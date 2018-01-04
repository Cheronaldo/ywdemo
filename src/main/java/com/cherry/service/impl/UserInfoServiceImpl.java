package com.cherry.service.impl;

import com.cherry.converter.UserInfo2SiteUserInfoVOConverter;
import com.cherry.dataobject.AgencySiteRelationship;
import com.cherry.dataobject.UserInfo;
import com.cherry.enums.UserEnum;
import com.cherry.form.UserInfoForm;
import com.cherry.form.UserUpdateForm;
import com.cherry.repository.AgencySiteRelationshipRepository;
import com.cherry.repository.UserInfoRepository;
import com.cherry.repository.UserLevelRepository;
import com.cherry.service.UserInfoService;
import com.cherry.util.DateUtil;
import com.cherry.util.KeyUtil;
import com.cherry.vo.SiteUserInfoVO;
import com.cherry.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/11/07.
 */
@Service
public class UserInfoServiceImpl implements UserInfoService{

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private UserLevelRepository levelRepository;

    @Autowired
    private AgencySiteRelationshipRepository relationshipRepository;

    @Override
    public Integer saveUser(UserInfoForm userInfoForm) {

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoForm, userInfo);
        // 注意将用户类型转换为对应的 类型码再储存 否则userInfo中 userClass为 null
        int userClass = levelRepository.findByClassInfo(userInfoForm.getUserClass()).getUserClass();
        userInfo.setUserClass(userClass);

        try {
            repository.save(userInfo);
        }catch (Exception e){
            return 1;
        }
        return 0;
    }

    @Override
    public Integer userLogin(String userName, String userPassword) {

        // 1.查询密码是否存在
        UserInfo userInfo = repository.findOne(userName);
        if (userInfo == null){
            //该用户名不存在
            return 1;
        }

        // 2.密码一致性校验
        if (!userPassword.equals(userInfo.getUserPassword())){
            //输入密码与数据库用户密码不一致
            return 1;
        }

        // 3.添加用户信息至session(放到controller层)

        return 0;
    }

    @Override
    public UserInfoVO getUserInfoVOByUserName(String userName) {

        UserInfo userInfo = repository.findOne(userName);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo, userInfoVO);
        userInfoVO.setUserClass(levelRepository.findByUserClass(userInfo.getUserClass()).getClassInfo());

        return userInfoVO;

    }

    @Override
    public UserInfo getUserInfoByUserName(String userName) {
        return repository.findOne(userName);
    }

    @Override
    public Integer updateUserInfo(UserUpdateForm form) {
        // 1.查询获取用户基本信息记录
        UserInfo userInfo = repository.findOne(form.getUserName());
        // 2.将表单属性赋给基本信息记录
        BeanUtils.copyProperties(form, userInfo);
        // 3.注意将用户类型转换为对应的 类型码再储存 否则userInfo中 userClass为 null
        int userClass = levelRepository.findByClassInfo(form.getUserClass()).getUserClass();
        userInfo.setUserClass(userClass);
        // 4.保存修改后的信息记录
        try {
            repository.save(userInfo);
        }catch (Exception e){
            return 1;
        }
        return 0;
    }

    @Override
    public Integer updateUserPassword(String userName, String userPassword) {
        // 1.查询获取用户基本信息记录
        UserInfo userInfo = repository.findOne(userName);
        // 2.修改密码
        userInfo.setUserPassword(userPassword);
        // 3.保存记录
        try {
            repository.save(userInfo);
        }catch (Exception e){
            return 1;
        }
        return 0;
    }

    @Override
    public Map<String, Object> getSiteUserList(String userName, Pageable pageable) {

        Map<String, Object> map = new HashMap<>();
        // 1.通过经销商用户名 获取 现场用户列表
        List<AgencySiteRelationship> relationshipList = relationshipRepository.findByAgencyUserNameAndIsUsed(userName, 1);
        if (relationshipList.size() == 0){
            // 没有记录
            map.put("code", 1);
            map.put("msg", UserEnum.NO_RECORDS_FOUND.getMessage());

            return map;
        }

        // 2.根据用户名列表 获取相关信息
        List<String> siteUserList = relationshipList.stream().map(e ->
                e.getSiteUserName()
        ).collect(Collectors.toList());

        Page<UserInfo> userInfoPage = repository.findByUserNameIn(siteUserList, pageable);

        if (userInfoPage.getContent() == null){
            // 没有记录
            map.put("code", 1);
            map.put("msg", UserEnum.NO_RECORDS_FOUND.getMessage());

            return map;
        }

        // 3.封装为 现场用户 VO对象
        List<SiteUserInfoVO> siteUserInfoVOList = UserInfo2SiteUserInfoVOConverter.convert(userInfoPage.getContent());

        map.put("code", 0);
        map.put("msg", UserEnum.GET_RECORDS_SUCCESS.getMessage());
        map.put("total", userInfoPage.getTotalPages());
        map.put("records", userInfoPage.getTotalElements());
        map.put("data", siteUserInfoVOList);

        return map;
    }

    @Override
    @Transactional
    public Integer addSiteUser(UserInfoForm form, String agencyName) {

        // 1.查询用户名是否存在且启用
        UserInfo userInfo = repository.findByUserNameAndIsUsed(form.getUserName(), 1);
        if (userInfo != null){
            // 用户名存在
            return 1;
        }

        // 2.添加用户信息表记录
        UserInfo userInfoOld = repository.findOne(form.getUserName());
        if (userInfoOld != null){
            // 用户名存在 修改信息 重新启用
            BeanUtils.copyProperties(form, userInfoOld);
            userInfoOld.setIsUsed(1);
            userInfoOld.setUserClass(3);
            repository.save(userInfoOld);
        } else {
            // 用户名不存在 添加用户信息表
            UserInfo userInfoNew = new UserInfo();
            BeanUtils.copyProperties(form, userInfoNew);
            userInfoNew.setIsUsed(1);
            userInfoNew.setUserClass(3);
            repository.save(userInfoNew);
        }

        // 3.添加经销商 现场 用户关系表 记录
        AgencySiteRelationship relationship = relationshipRepository.findBySiteUserName(form.getUserName());
        if (relationship != null){
            // 关系记录存在 修改信息 重新启用
            relationship.setAgencyUserName(agencyName);
            relationship.setIsUsed(1);
            relationship.setRegisterTime(DateUtil.getDate());
            relationshipRepository.save(relationship);

            return 0;
        }

        // 关系记录不存在 添加记录
        AgencySiteRelationship relationshipAdd = new AgencySiteRelationship();
        relationshipAdd.setId(KeyUtil.genUniqueKey());
        relationshipAdd.setSiteUserName(form.getUserName());
        relationshipAdd.setAgencyUserName(agencyName);
        relationshipAdd.setIsUsed(1);
        relationshipAdd.setRegisterTime(DateUtil.getDate());

        relationshipRepository.save(relationshipAdd);

        return 0;
    }

    @Override
    @Transactional
    public Integer unbindSiteUser(String userName) {
        // 1.查询从属关系记录 并修改 启用标志
        AgencySiteRelationship relationship = relationshipRepository.findBySiteUserName(userName);
        if (relationship != null){
            // 置启用标志
            relationship.setIsUsed(0);

            relationshipRepository.save(relationship);
        }
        // 2.查询信息表 并修改 启用标志
        UserInfo userInfo = repository.findOne(userName);
        if (userInfo != null){
            // 置启用标志
            userInfo.setIsUsed(0);

            repository.save(userInfo);
        }

        return 0;
    }
}

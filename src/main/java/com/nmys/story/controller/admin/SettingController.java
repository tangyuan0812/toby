package com.nmys.story.controller.admin;

import com.nmys.story.constant.WebConstant;
import com.nmys.story.controller.BaseController;
import com.nmys.story.exception.TipException;
import com.nmys.story.model.bo.BackResponseBo;
import com.nmys.story.model.bo.RestResponseBo;
import com.nmys.story.model.entity.Options;
import com.nmys.story.service.IOptionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/setting")
public class SettingController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingController.class);

    @Autowired
    private IOptionService optionService;

    /**
     * 系统设置
     */
    @GetMapping(value = "")
    public String setting(HttpServletRequest request) {
        List<Options> voList = optionService.getOptions();
        Map<String, String> options = new HashMap<>();
        voList.forEach((option) -> {
            options.put(option.getName(), option.getValue());
        });
        if (options.get("site_record") == null) {
            options.put("site_record", "");
        }
        request.setAttribute("options", options);
        return "admin/setting";
    }

    /**
     * Description:保存系统设置
     * Author:70kg
     * Param [site_theme, request]
     * Return com.nmys.story.model.bo.RestResponseBo
     * Date 2018/5/21 10:28
     */
    @PostMapping(value = "")
    @ResponseBody
    @RequiresRoles("admin")
    public RestResponseBo saveSetting(@RequestParam(required = false) String site_theme,
                                      HttpServletRequest request) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, String> querys = new HashMap();
            parameterMap.forEach((key, value) -> {
                querys.put(key, join(value));
            });
            optionService.saveOrUpdateOptions(querys);
            WebConstant.initConfig = querys;
            if (StringUtils.isNotBlank(site_theme)) {
                BaseController.THEME = "themes/" + site_theme;
            }
            return RestResponseBo.ok();
        } catch (Exception e) {
            String msg = "保存设置失败";
            return RestResponseBo.fail(msg);
        }
    }

    /**
     * 数组转字符串
     *
     * @param arr
     * @return
     */
    private String join(String[] arr) {
        StringBuilder ret = new StringBuilder();
        String[] var3 = arr;
        int var4 = arr.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String item = var3[var5];
            ret.append(',').append(item);
        }

        return ret.length() > 0 ? ret.substring(1) : ret.toString();
    }


    /**
     * Description:备份数据库
     * Author:70kg
     * Param [bk_type:类型, bk_path:存放位置, request]
     * Return com.nmys.story.model.bo.RestResponseBo
     * Date 2018/5/31 9:51
     */
    @PostMapping(value = "backup")
    @ResponseBody
    @RequiresRoles("admin")
    public RestResponseBo backup(@RequestParam String bk_type,
                                 @RequestParam String bk_path,
                                 HttpServletRequest request) {
        if (StringUtils.isBlank(bk_path)) {
            return RestResponseBo.fail("请输入sql文件存放路径");
        }
        try {
            BackResponseBo backResponse = optionService.backup(bk_type, bk_path, "yyyy-MM-dd");
            return RestResponseBo.ok(backResponse);
        } catch (Exception e) {
            String msg = "备份失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
    }

}

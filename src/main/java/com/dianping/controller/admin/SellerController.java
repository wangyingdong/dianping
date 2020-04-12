package com.dianping.controller.admin;

import com.dianping.annotation.AdminPermission;
import com.dianping.common.exception.BusinessException;
import com.dianping.model.Seller;
import com.dianping.service.SellerService;
import com.dianping.vo.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller("/admin/seller")
@RequestMapping("/admin/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    //商户列表
    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(Page page) {

        ModelAndView modelAndView = new ModelAndView("/admin/seller/index");
        PageInfo<Seller> pageInfo = PageHelper.startPage(page).doSelectPageInfo(() -> {
            sellerService.selectAll();
        });

        modelAndView.addObject("data", pageInfo);
        modelAndView.addObject("CONTROLLER_NAME", "seller");
        modelAndView.addObject("ACTION_NAME", "index");
        return modelAndView;
    }

    @RequestMapping("/createpage")
    @AdminPermission
    public ModelAndView createPage() {
        ModelAndView modelAndView = new ModelAndView("/admin/seller/create");
        modelAndView.addObject("CONTROLLER_NAME", "seller");
        modelAndView.addObject("ACTION_NAME", "create");
        return modelAndView;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid Seller sellerCreateReq) throws BusinessException {
        sellerService.create(sellerCreateReq);
        return "redirect:/admin/seller/index";
    }

    @RequestMapping(value = "down", method = RequestMethod.POST)
    @AdminPermission
    @ResponseBody
    public Seller down(@RequestParam(value = "id") Integer id) throws BusinessException {
        Seller sellerModel = sellerService.changeStatus(id, 1);
        return sellerModel;
    }

    @RequestMapping(value = "up", method = RequestMethod.POST)
    @AdminPermission
    @ResponseBody
    public Seller up(@RequestParam(value = "id") Integer id) throws BusinessException {
        Seller sellerModel = sellerService.changeStatus(id, 0);
        return sellerModel;
    }


}

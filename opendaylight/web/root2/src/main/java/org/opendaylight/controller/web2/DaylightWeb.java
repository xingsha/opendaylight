/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.web2;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.opendaylight.controller.sal.utils.ServiceHelper;
import org.opendaylight.controller.usermanager.IUserManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DaylightWeb {
    @RequestMapping(value = "")
    public String index(Model model, HttpServletRequest request) {
        IUserManager userManager = (IUserManager) ServiceHelper
                .getGlobalInstance(IUserManager.class, this);
        if (userManager == null) {
            return "User Manager is not available";
        }

        String username = request.getUserPrincipal().getName();

        model.addAttribute("username", username);
        model.addAttribute("role", userManager.getUserLevel(username)
                .toNumber());

        return "main";
    }

    @RequestMapping(value = "logout")
    public String logout(Map<String, Object> model, final HttpServletRequest request) {

        IUserManager userManager = (IUserManager) ServiceHelper
                .getGlobalInstance(IUserManager.class, this);
        if (userManager == null) {
            return "User Manager is not available";
        }
        String username = request.getUserPrincipal().getName();
        HttpSession session = request.getSession(false);
        if (session != null) {
            if (username != null) {
                userManager.userLogout(username);
            }
            session.invalidate();

        }
        return "redirect:" + "/";
    }

    @RequestMapping(value = "login")
    public String login(Model model, final HttpServletRequest request,
            final HttpServletResponse response) {
        // response.setHeader("X-Page-Location", "/login");
        IUserManager userManager = (IUserManager) ServiceHelper
                .getGlobalInstance(IUserManager.class, this);
        if (userManager == null) {
            return "User Manager is not available";
        }

        String username = request.getUserPrincipal().getName();

        model.addAttribute("username", username);
        model.addAttribute("role", userManager.getUserLevel(username)
                .toNumber());
        return "forward:" + "/";
    }

}

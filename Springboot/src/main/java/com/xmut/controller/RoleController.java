package com.xmut.controller;

import com.xmut.entity.Role;
import com.xmut.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "角色管理")
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation("Get all roles")
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @ApiOperation("Add role")
    @PostMapping
    public String addRole(@RequestBody Role role) {
        boolean result = roleService.addRole(role);
        return result ? "Add successful" : "Add failed";
    }

    @ApiOperation("Update role information")
    @PutMapping
    public String updateRole(@RequestBody Role role) {
        boolean result = roleService.updateRole(role);
        return result ? "Update successful" : "Update failed";
    }

    @ApiOperation("Delete role")
    @DeleteMapping("/{roleId}")
    public String deleteRole(@PathVariable Integer roleId) {
        boolean result = roleService.deleteRole(roleId);
        return result ? "Delete successful" : "Delete failed";
    }
}
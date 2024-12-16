package com.xmut.controller;

import com.xmut.entity.Permission;
import com.xmut.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "权限管理")
@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation("Get all permissions")
    @GetMapping
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @ApiOperation("Add permission")
    @PostMapping
    public String addPermission(@RequestBody Permission permission) {
        boolean result = permissionService.addPermission(permission);
        return result ? "Add successful" : "Add failed";
    }

    @ApiOperation("Update permission information")
    @PutMapping
    public String updatePermission(@RequestBody Permission permission) {
        boolean result = permissionService.updatePermission(permission);
        return result ? "Update successful" : "Update failed";
    }

    @ApiOperation("Delete permission")
    @DeleteMapping("/{permissionId}")
    public String deletePermission(@PathVariable Integer permissionId) {
        boolean result = permissionService.deletePermission(permissionId);
        return result ? "Delete successful" : "Delete failed";
    }
}
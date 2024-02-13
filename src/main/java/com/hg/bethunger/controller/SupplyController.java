package com.hg.bethunger.controller;

import com.hg.bethunger.dto.SupplyCreateDTO;
import com.hg.bethunger.dto.SupplyDTO;
import com.hg.bethunger.security.UserPrincipal;
import com.hg.bethunger.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/supplies")
public class SupplyController {
    private final SupplyService supplyService;

    @Autowired
    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplyDTO createSupply(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody SupplyCreateDTO dto) {
        return supplyService.createSupply(userPrincipal.getUser(), dto);
    }
}

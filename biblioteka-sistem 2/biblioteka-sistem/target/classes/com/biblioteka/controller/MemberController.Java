// MemberController.java
package com.biblioteka.controller;

import com.biblioteka.model.Member;
import com.biblioteka.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping("/members")
public class MemberController {
    
    @Autowired
    private MemberRepository memberRepository;
    
    @GetMapping
    public String listMembers(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "members/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("member", new Member());
        return "members/form";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Neispravan član ID: " + id));
        model.addAttribute("member", member);
        return "members/form";
    }
    
    @PostMapping("/save")
    public String saveMember(@ModelAttribute Member member) {
        if (member.getId() == null) {
            member.setMembershipNumber("MEM" + UUID.randomUUID().toString().substring(0, 8));
            member.setJoinDate(LocalDate.now());
            member.setExpiryDate(LocalDate.now().plusYears(1));
        }
        memberRepository.save(member);
        return "redirect:/members";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberRepository.deleteById(id);
        return "redirect:/members";
    }
}

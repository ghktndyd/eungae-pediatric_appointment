package com.playdata.eungae.member.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.playdata.eungae.file.FileStore;
import com.playdata.eungae.file.ResultFileStore;
import com.playdata.eungae.member.dto.ChildrenDto;
import com.playdata.eungae.member.dto.ChildrenRequestDto;
import com.playdata.eungae.member.service.ChildrenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/children")
public class ChildrenController {

	private final ChildrenService childrenService;
	private final FileStore fileStore;


	@GetMapping("/list")
	public String getAllChildren(Model model) {
		List<ChildrenDto> childrenList = childrenService.getAllChildren();
		model.addAttribute("childrenList", childrenList);
		return "contents/member/my-children";
	}

	@GetMapping("/form")
	public String addChildrenForm(Model model) {
		model.addAttribute("childrenDto", new ChildrenDto());
		return "contents/member/my-children-add";
	}

	@PostMapping("/form")
	public String createChild(ChildrenRequestDto childrenRequestDto,
		 MultipartFile profileImage,
		@AuthenticationPrincipal UserDetails member) throws IOException {
		ResultFileStore resultFileStore;
		resultFileStore = fileStore.storeFile(profileImage);
		String email = member.getUsername();
		childrenService.createChildren(childrenRequestDto, resultFileStore, email);
		return "redirect:/children/list";
	}

	@PostMapping("/{id}")
	public String deleteChild(@PathVariable Long id) {
		childrenService.deleteChild(id);
		return "redirect:/children/list"; // 자녀 목록 페이지로 리디렉션
	}

}
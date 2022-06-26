package com.example.tripletest.controller;


import com.example.tripletest.ReviewEventDto;
import com.example.tripletest.entity.Members;
import com.example.tripletest.repository.MembersRepository;
import com.example.tripletest.service.EventService;
import com.example.tripletest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class EventController {

    private final EventService eventService;
    private final MemberService memberService;

    @PostMapping("/events")
    public ResponseEntity<String> CommentEvents (@RequestBody ReviewEventDto dto){
        eventService.pointEvent(dto);
        return ResponseEntity.ok("SUCCESS");
    }

    @GetMapping("/points/{userId}")
    public ResponseEntity<Integer> getEventPoint(@PathVariable String userId) {
        //포인트 조회 API
        Members member = memberService.findMember(userId);
        return ResponseEntity.ok(member.getTotalPoint());
    }

}

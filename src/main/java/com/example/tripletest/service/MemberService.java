package com.example.tripletest.service;

import com.example.tripletest.entity.Members;
import com.example.tripletest.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MembersRepository membersRepository;

    public Members saveMember(String userId){
        Members member = new Members(userId);
        membersRepository.save(member);
        return member;
    }

    //회원 찾는거
    public Members findMember(String userId){
        return membersRepository.findByUserId(userId).orElseThrow(
                () -> new NullPointerException("일치하는 회원을 찾을 수 없습니다.")
        );
    }

}

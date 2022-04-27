package com.disclaimedgoat.Utilities;

import net.dv8tion.jda.api.entities.Member;

public class MemberUtils {

    public static String memberToMentionable(Member member) {
        return memberToMentionable(member.getId());
    }

    public static String memberToMentionable(String memberId) {
        return String.format("<@%s>", memberId);
    }

}

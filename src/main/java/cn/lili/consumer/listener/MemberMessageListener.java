package cn.lili.consumer.listener;

import cn.hutool.json.JSONUtil;
import cn.lili.common.event.MemberEvent;
import cn.lili.consumer.event.*;
import cn.lili.modules.connect.entity.dto.MemberConnectLoginMessage;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.entity.dos.MemberSign;
import cn.lili.modules.member.entity.dto.MemberPointMessage;
import cn.lili.modules.member.service.MemberSignService;
import cn.lili.modules.wallet.entity.dto.MemberWithdrawalMessage;
import cn.lili.rocketmq.tags.MemberTagsEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 会员消息
 *
 * @author paulG
 * @since 2020/12/9
 **/
@Component
@Slf4j
public class MemberMessageListener  {


    /**
     * 会员签到
     */
    @Autowired
    private MemberSignService memberSignService;
    /**
     * 会员积分变化
     */
    @Autowired
    private List<MemberPointChangeEvent> memberPointChangeEvents;
    /**
     * 会员提现
     */
    @Autowired
    private List<MemberWithdrawalEvent> memberWithdrawalEvents;
    /**
     * 会员注册
     */
    @Autowired
    private List<MemberRegisterEvent> memberSignEvents;

    /**
     * 会员注册
     */
    @Autowired
    private List<MemberLoginEvent> memberLoginEvents;
    @Autowired
    private List<MemberInfoChangeEvent> memberInfoChangeEvents;
    @Autowired
    private List<MemberConnectLoginEvent> memberConnectLoginEvents;

    @EventListener(MemberEvent.class)
    public void onMessage(MemberEvent evt) {
     MemberTagsEnum tagsEnum = evt.getTag();
     String body = evt.getBody();
     switch (tagsEnum) {
            //会员注册
            case MEMBER_REGISTER:
                for (MemberRegisterEvent memberRegisterEvent : memberSignEvents) {
                    try {
                        Member member = JSONUtil.toBean(body, Member.class);
                        memberRegisterEvent.memberRegister(member);
                    } catch (Exception e) {
                        log.error("会员{},在{}业务中，状态修改事件执行异常",
                                body,
                                memberRegisterEvent.getClass().getName(),
                                e);
                    }
                }
                break;
            //用户登录
            case MEMBER_LOGIN:

                for (MemberLoginEvent memberLoginEvent : memberLoginEvents) {
                    try {
                        Member member = JSONUtil.toBean(body, Member.class);
                        memberLoginEvent.memberLogin(member);
                    } catch (Exception e) {
                        log.error("会员{},在{}业务中，状态修改事件执行异常",
                                body,
                                memberLoginEvent.getClass().getName(),
                                e);
                    }
                }
                break;
            //会员签到
            case MEMBER_SING:
                MemberSign memberSign = JSONUtil.toBean(body, MemberSign.class);
                memberSignService.memberSignSendPoint(memberSign.getMemberId(), memberSign.getSignDay());
                break;
            //会员积分变动
            case MEMBER_POINT_CHANGE:
                for (MemberPointChangeEvent memberPointChangeEvent : memberPointChangeEvents) {
                    try {
                        MemberPointMessage memberPointMessage = JSONUtil.toBean(body, MemberPointMessage.class);
                        memberPointChangeEvent.memberPointChange(memberPointMessage);
                    } catch (Exception e) {
                        log.error("会员{},在{}业务中，状态修改事件执行异常",
                                body,
                                memberPointChangeEvent.getClass().getName(),
                                e);
                    }
                }
                break;
            //会员信息更改
            case MEMBER_INFO_EDIT:
                for (MemberInfoChangeEvent memberInfoChangeEvent : memberInfoChangeEvents) {
                    try {
                        Member member = JSONUtil.toBean(body, Member.class);
                        memberInfoChangeEvent.memberInfoChange(member);
                    } catch (Exception e) {
                        log.error("会员{},在{}业务中，提现事件执行异常",
                                body,
                                memberInfoChangeEvent.getClass().getName(),
                                e);
                    }
                }
                break;
            //会员提现
            case MEMBER_WITHDRAWAL:
                for (MemberWithdrawalEvent memberWithdrawalEvent : memberWithdrawalEvents) {
                    try {
                        MemberWithdrawalMessage memberWithdrawalMessage = JSONUtil.toBean(body, MemberWithdrawalMessage.class);
                        memberWithdrawalEvent.memberWithdrawal(memberWithdrawalMessage);
                    } catch (Exception e) {
                        log.error("会员{},在{}业务中，提现事件执行异常",
                                body,
                                memberWithdrawalEvent.getClass().getName(),
                                e);
                    }
                }
                break;
            //用户第三方登录
            case MEMBER_CONNECT_LOGIN:
                for (MemberConnectLoginEvent memberConnectLoginEvent : memberConnectLoginEvents) {
                    try {
                        MemberConnectLoginMessage memberConnectLoginMessage = JSONUtil.toBean(body, MemberConnectLoginMessage.class);
                        memberConnectLoginEvent.memberConnectLogin(memberConnectLoginMessage.getMember(), memberConnectLoginMessage.getConnectAuthUser());
                    } catch (Exception e) {
                        log.error("会员{},在{}业务中，状态修改事件执行异常",
                                body,
                                memberConnectLoginEvent.getClass().getName(),
                                e);
                    }
                }
                break;
            default:
                break;
        }
    }
}

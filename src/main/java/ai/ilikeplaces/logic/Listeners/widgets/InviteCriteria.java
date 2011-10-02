package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.logic.contactimports.ImportedContact;
import ai.ilikeplaces.logic.validators.unit.HumanId;

/**
 * Given a requirement to invite someone onboard, this class is a container of the information required to do the invite
 *
* Created by IntelliJ IDEA.
* User: Ravindranath Akila
* Date: 10/2/11
* Time: 2:00 AM
*/
public class InviteCriteria {
    private final String displayName;
    private final String profileUrl;
    private final String profilePhoto;
    private final HumanId inviter;
    private final ImportedContact invitee;

    /**
     * @param displayName
     * @param inviteesProfileUrl
     * @param inviteesProfilePhoto
     * @param inviter
     * @param invitee
     */
    InviteCriteria(final String displayName,
                   final String inviteesProfileUrl,
                   final String inviteesProfilePhoto,
                   final HumanId inviter,
                   final ImportedContact invitee) {
        this.displayName = displayName;
        this.profileUrl = inviteesProfileUrl;
        this.profilePhoto = inviteesProfilePhoto;
        this.inviter = inviter;
        this.invitee = invitee;
    }

    public String getInviterDisplayName() {
        return displayName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public HumanId getInviter() {
        return inviter;
    }

    public ImportedContact getInvitee() {
        return invitee;
    }
}

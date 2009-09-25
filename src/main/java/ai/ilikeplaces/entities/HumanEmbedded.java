/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ilikeplaces.entities;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.Embeddable;
import javax.persistence.Id;

/**
 *
 * @author Ravindranath Akila
 */
@Embeddable
public class HumanEmbedded implements Serializable {

//    public static final Integer genderCodeNeutral = 0;
//    public static final Integer genderCodeMale = 1;
//    public static final Integer genderCodeFemale = 2;
    private String humanIdentification;
    
//    private String loggedOnUserPassword;
//    private String loggedOnUserEmail;
//    private Integer loggedOnUserGenderCode;
//    private Date loggedOnUserDateOfBirth;
//    private String loggedOnUserGUIPreferences;
//    private String loggedOnUserAuthorizations;
//    private List<Human> loggedOnUserKnowns;
//    private List<Human> loggedOnUserProfessionals;
//    private List<Human> loggedOnUserFriends;
//    private List<Human> loggedOnUserBestFriends;
//    private List<Human> loggedOnUserRelations;
//    private List<Human> loggedOnUserFamilyMembers;

//    static public enum GenderCode {
//
//        NEUTRAL(0), MALE(1), FEMALE(2);
//        final private int GenderCodeValue_;
//
//        private GenderCode(final int GenderCode__) {
//            this.GenderCodeValue_ = GenderCode__;
//        }
//
//        public int getGenderCode() {
//            return this.GenderCodeValue_;
//        }
//
//        final static public GenderCode valueOf(final String genderString__, final boolean ingonreCase) throws IllegalEnumValueException {
//            GenderCode returnVal_;
//            try {
//                if (!ingonreCase) {
//                    returnVal_ = valueOf(genderString__);
//                } else {
//                    returnVal_ = valueOf(genderString__.toUpperCase());
//                }
//            } catch (final IllegalArgumentException e__) {
//                throw new IllegalEnumValueException(genderString__);
//            }
//            return returnVal_;
//        }
//
//        final static public class IllegalEnumValueException extends Exception {
//
//            IllegalEnumValueException(final String genderString__) {
//                super("SORRY, NO SUCH ENUM EXISTS THAT YOU GAVE ME TO PROCESS AS(EVEN WHEN CONVERTED TO UPPERCASE):" + genderString__);
//            }
//        }
//    }

    @Id
    public String getHumanIdentification() {
        return humanIdentification;
    }

    public void setHumanIdentification(final String humanIdentification__) {
        this.humanIdentification = humanIdentification__;
    }

//    public String getLoggedOnUserPassword() {
//        return loggedOnUserPassword;
//    }
//
//    public void setLoggedOnUserPassword(final String loggedOnUserPassword) {
//        this.loggedOnUserPassword = loggedOnUserPassword;
//    }
//
//    public String getLoggedOnUserAuthorizations() {
//        return loggedOnUserAuthorizations;
//    }
//
//    public void setLoggedOnUserAuthorizations(final String loggedOnUserAuthorizations) {
//        this.loggedOnUserAuthorizations = loggedOnUserAuthorizations;
//    }
//
//    @Temporal(javax.persistence.TemporalType.DATE)
//    public Date getLoggedOnUserDateOfBirth() {
//        return loggedOnUserDateOfBirth;
//    }
//
//    public void setLoggedOnUserDateOfBirth(final Date loggedOnUserDateOfBirth) {
//        this.loggedOnUserDateOfBirth = loggedOnUserDateOfBirth;
//    }
//
//    public String getLoggedOnUserGUIPreferences() {
//        return loggedOnUserGUIPreferences;
//    }
//
//    public void setLoggedOnUserGUIPreferences(final String loggedOnUserGUIPreferences) {
//        this.loggedOnUserGUIPreferences = loggedOnUserGUIPreferences;
//    }
//
//    public Integer getLoggedOnUserGenderCode() {
//        return loggedOnUserGenderCode;
//    }
//
//    public void setLoggedOnUserGenderCode(final Integer loggedOnUserGenderCode) {
//        this.loggedOnUserGenderCode = loggedOnUserGenderCode;
//    }
//
//    public String getLoggedOnUserEmail() {
//        return loggedOnUserEmail;
//    }
//
//    public void setLoggedOnUserEmail(final String loggedOnUserEmail) {
//        this.loggedOnUserEmail = loggedOnUserEmail;
//    }
//
//    @OneToMany
//    public List<Human> getLoggedOnUserBestFriends() {
//        return loggedOnUserBestFriends;
//    }
//
//    public void setLoggedOnUserBestFriends(final List<Human> loggedOnUserBestFriends) {
//        this.loggedOnUserBestFriends = loggedOnUserBestFriends;
//    }
//
//    @OneToMany
//    public List<Human> getLoggedOnUserFamilyMembers() {
//        return loggedOnUserFamilyMembers;
//    }
//
//    public void setLoggedOnUserFamilyMembers(final List<Human> loggedOnUserFamilyMembers) {
//        this.loggedOnUserFamilyMembers = loggedOnUserFamilyMembers;
//    }
//
//    @OneToMany
//    public List<Human> getLoggedOnUserFriends() {
//        return loggedOnUserFriends;
//    }
//
//    public void setLoggedOnUserFriends(final List<Human> loggedOnUserFriends) {
//        this.loggedOnUserFriends = loggedOnUserFriends;
//    }
//
//    @OneToMany
//    public List<Human> getLoggedOnUserKnowns() {
//        return loggedOnUserKnowns;
//    }
//
//    public void setLoggedOnUserKnowns(final List<Human> loggedOnUserKnowns__) {
//        this.loggedOnUserKnowns = loggedOnUserKnowns__;
//    }
//
//    @OneToMany
//    public List<Human> getLoggedOnUserProfessionals() {
//        return loggedOnUserProfessionals;
//    }
//
//    public void setLoggedOnUserProfessionals(final List<Human> loggedOnUserProfessionals__) {
//        this.loggedOnUserProfessionals = loggedOnUserProfessionals__;
//    }
//
//    @OneToMany
//    public List<Human> getLoggedOnUserRelations() {
//        return loggedOnUserRelations;
//    }
//
//    public void setLoggedOnUserRelations(final List<Human> loggedOnUserRelations_) {
//        this.loggedOnUserRelations = loggedOnUserRelations_;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (humanIdentification != null ? humanIdentification.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HumanEmbedded other = (HumanEmbedded) obj;
        if (this.humanIdentification != other.humanIdentification && (this.humanIdentification == null || !this.humanIdentification.equals(other.humanIdentification))) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return toString_
     */
    @Override
    public String toString() {
        String toString_ = new String(getClass().getName());
        try {
            final Field[] fields = {getClass().getDeclaredField("loggedOnUserId")};

            for (final Field field : fields) {
                try {
                    toString_ += "\n{" + field.getName() + "," + field.get(this) + "}";
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Location.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Location.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(Location.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Location.class.getName()).log(Level.SEVERE, null, ex);
        }

        return toString_;
    }

    /**
     *
     * @param showChangeLog__
     * @return changeLog
     */
    public String toString(final boolean showChangeLog__) {
        String changeLog = new String(toString() + "\n");
        changeLog += "20090914 Initial \n";
        changeLog += "20090918 Added enum for user gender and many other variables \n";
        return showChangeLog__ ? changeLog : toString();
    }
}

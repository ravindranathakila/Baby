package ai.ilikeplaces.util;

import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.GeoCoord;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.SimpleString;

public class UserIntroduction {
    public static void createIntroData(HumanId newUser) {

        SettingMoment:
        {
            final Return<PrivateLocation> privateLocationReturn = DB.getHumanCrudPrivateLocationLocal(false).cPrivateLocation(
                    newUser,
                    new SimpleString("Taj Mahal"),
                    new SimpleString("The symbol of love"),
                    (new GeoCoord().setObj("27.17311262887182,78.04209488227843")));

            final Return<PrivateEvent> privateEventReturn = DB.getHumanCrudPrivateEventLocal(false).cPrivateEvent(
                    newUser.getHumanId(),
                    privateLocationReturn.returnValue().getPrivateLocationId(),
                    "Trotting Taj Mahal",
                    "and feelin' the luv!",
                    "TODO",
                    "TODO");

            DB.getHumanCrudPrivateEventLocal(false).addEntryToWall(
                    newUser,
                    newUser,
                    new Obj<Long>(privateEventReturn.returnValue().getPrivateEventId()),
                    "I just started a moment at Taj Mahal!");

            DB.getHumanCrudPrivateEventLocal(false).addEntryToWall(
                    newUser,
                    newUser,
                    new Obj<Long>(privateEventReturn.returnValue().getPrivateEventId()),
                    "I need to add my friends http://www.ilikeplaces.com/page/_friends so that I can invite them to this moment.\"");
        }

        SettingPersonalWall:
        {
            DB.getHumanCrudWallLocal(false).addEntryToWall(
                    newUser,
                    newUser,
                    new Obj<HumanId>(newUser),
                    "I just joined I Like Places - Down Town!");
        }

    }
}
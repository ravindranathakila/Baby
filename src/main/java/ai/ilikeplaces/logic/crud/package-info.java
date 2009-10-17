@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@NOTE(note = "This package handles all db cruds.")
@CONVENTION(convention = "Plenty",
conventions = {
    "C=Create\n" +
    "R=Read\n" +
    "U=Update\n" +
    "D=Delete",
    "Public classes here are always one entity handling " +
    "another. For example A creates B, so the class ACB is public.\n" +
    "The rest of the classes are visible only to the package.",
    "CrudeService in injected only to the package visible" +
    " only classes. In most cases, there are pure entitiy handles. i.e. NOT one" +
    " entity handling a nother.",
    "As\n" +
    "C=Create\n" +
    "R=Read\n" +
    "U=Update\n" +
    "D=Delete\n" +
    "Implies the operation, instead of saying set or get, we say do. For" +
    " Example, doEntity1UEntityB, which implies \"Do Entity 1 Update Entity2."})
package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.CONVENTION;

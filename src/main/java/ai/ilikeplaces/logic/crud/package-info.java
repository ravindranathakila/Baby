@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@NOTE(note = "This package handles all db cruds.")
@CONVENTION(convention = "Plenty",
conventions = {
    "C=Create\n"
    + "R=Read\n"
    + "U=Update\n"
    + "D=Delete",
    "Public classes here are always one entity handling "
    + "another. For example A creates B, so the class ACB is public.\n"
    + "The rest of the classes are visible only to the package.",
    "CrudService in injected only to the package visible"
    + " only classes. In most cases, there are pure entitiy handles. i.e. NOT one"
    + " entity handling a nother.",
    "CrudService never creates a transaction. It SUPPORTS and MANDATORY. Hence "
    + "the callers should handle the transactions. The classes containing CrudService "
    + "always ENFORCE a transaction level. i.e. MANDATORY, NEVER. "
    + "Others always define a transaction lever, REQUIRES_NEW, NOT_SUPPORTED, REQUIRED"
    + "Please note that each one of these will have a seperate method such as doCreate, doDirtyCreate, doNTxCreate "
    + "whete NTx stands for transactional(meaning do it in a new transaction). "
    + "Always remember that database cleanliness(handling null fields) is done by classes "
    + "containing CrudService",
    "As\n"
    + "C=Create\n"
    + "R=Read\n"
    + "U=Update\n"
    + "D=Delete\n"
    + "Implies the operation, instead of saying set or get, we say do. For"
    + " Example, doEntity1UEntityB, which implies \"Do Entity 1 Update Entity2.",
    "The private classes show attitude \"I don't care who calls me, as long as I can "
    + "work on the database to make a consistant crud without leading to stale data\"."
    + "The public classes show attitude \"I do care who calls me. I will make sure "
    + "this guy can really do this operation, and if he sends me the correct data\".",
    "There should not be any exception thrown in this package which are conflics in database."
    + "Each logic should be checked and handle here regardless of if the caller does it."
    + "Threfore, if the caller makes a mistake, the exception thrown to him should be a tailormade one,"
    + "where as the error should NOT execute. Is simple words, no operation can break the database by calling"
    + "any claas in this package!"})
package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.CONVENTION;

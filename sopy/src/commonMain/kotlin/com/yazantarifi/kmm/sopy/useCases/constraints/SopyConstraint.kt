package com.yazantarifi.kmm.sopy.useCases.constraints

/**
 * Each UseCase has Input and Output
 * Any UseCase Might have Unit Input or Anything Else
 * We Need to Control The UseCases to Not Start them Until the Conditions Valid
 *
 * In this Case UseCase Should Check on the Constraints if Valid, UseCase gonna Start the Execution
 * If No, Will Throw Exception
 *
 * Example:
 * UseCase to Send Api Request and Map the Response by UserId and FilterKey
 * 1. Constraint to check on FilterKey is Valid -> Key to Check if Empty or Null and Check Filter Value if Can be Sent or Not
 * 2. Constraint to check on Valid Number Value -> UserId > 0 and Length > 8 for Example
 * If both Constraints are Valid, Api Request gonna be Called else UseCase Gonna Throw Exception
 */
abstract class SopifyConstraint<RequestValue> constructor(
    requestValue: RequestValue
): SopifyType {

    fun execute(onError: (Throwable) -> Unit) {
        if (!isConstraintValid()) {
            onError(getInvalidConstraintException())
        }
    }

}

interface SopifyType {

    /**
     * Logic Implementation of the Constraint Itself
     */
    fun isConstraintValid(): Boolean

    /**
     * Return Your Specific Exceptions and Messages, to control Your Client Code Better
     */
    fun getInvalidConstraintException(): Throwable

}
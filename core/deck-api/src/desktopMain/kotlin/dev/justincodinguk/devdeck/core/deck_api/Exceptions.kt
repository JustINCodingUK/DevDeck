package dev.justincodinguk.devdeck.core.deck_api

import dev.justincodinguk.devdeck.core.deck_api.refs.Platform

/**
 * Thrown when a .deckfile has invalid syntax
 * @param fileName Name of the faulty deckfile
 * @param line Line in which invalid syntax is found
 */
class DeckFileSyntaxException(fileName: String, line: String) :
    Exception("Invalid Syntax in DeckFile $fileName\n$line")

/**
 * Thrown when an installation reference cannot be found
 * @param refId The unique reference ID of the missing reference
 */
class MissingInstallReferenceException(refId: String) :
    Exception("No reference found for id $refId")

/**
 * Thrown when no suitable build can be found for the current [Platform]
 * @param id The unique reference ID of the installation which cannot be performed
 */
class NoBuildException(id: String) : Exception("No binary matching platform found for id $id")

/**
 * Thrown when an installation cannot proceed
 * @param id The unique reference ID of the installation which cannot be performed
 * @param reason The reason why the installation failed
 */
class InstallException(id: String, reason: String) :
    Exception("Error installing $id\n Reason: $reason")

/**
 * Thrown when an unsafe command is found in an installation reference
 * @param id The unique reference ID of the installation which contains an unsafe command
 */
class UnsafeCommandException(id: String) : Exception("Unsafe command found in $id")

/**
 * Thrown when the current package manager cannot be identified and no binary is found for the current [Platform]
 */
class UnknownPackageManagerException : Exception("Can't identify the system package manager")
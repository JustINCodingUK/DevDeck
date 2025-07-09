package dev.justincodinguk.devdeck.core.deck_api

class DeckFileSyntaxException(fileName: String, line: String) : Exception("Invalid Syntax in DeckFile $fileName\n$line")

class InstallReferenceError(refId: String) : Exception("No reference found for id $refId")

class NoBuildException(id: String) : Exception("No binary matching platform found for id $id")

class InstallException(id: String, reason: String) : Exception("Error installing $id\n Reason: $reason")

class UnsafeCommandException(id: String) : Exception("Unsafe command found in $id")

class UnknownPackageManagerException: Exception("Can't identify the system package manager")
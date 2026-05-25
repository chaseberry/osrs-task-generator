package com.chase.utilities

import kotlinx.serialization.Serializable

@Serializable
class WeirdGloopItem(
    override val id: Int,
    override val name: String,
    override val configName: String,
): WeirdGloopEntity
/*
{
    "id":0,
    "name":"Dwarf remains",
    "tradeable":false,
    "exchange":false,
    "members":true,
    "stackable":0,
    "value":1,
    "placeholderId":17851,
    "inventoryModel":2595,
    "examine":"The body of a Dwarf savaged by Goblins.",
    "weight":16000,
    "category":0,
    "invOps":["Destroy"],
    "ops":["Take"]
    ,"configName":"mcannonremains"
},
 */
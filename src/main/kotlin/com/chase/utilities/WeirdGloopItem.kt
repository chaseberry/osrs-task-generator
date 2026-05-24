package com.chase.utilities

import kotlinx.serialization.Serializable

@Serializable
class WeirdGloopItem(
    val id: Int,
    val name: String,
    val configName: String,
)
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
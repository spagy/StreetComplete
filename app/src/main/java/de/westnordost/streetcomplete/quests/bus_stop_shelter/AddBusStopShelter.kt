package de.westnordost.streetcomplete.quests.bus_stop_shelter

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType
import de.westnordost.streetcomplete.data.user.achievements.EditTypeAchievement.PEDESTRIAN
import de.westnordost.streetcomplete.osm.Tags
import de.westnordost.streetcomplete.osm.updateWithCheckDate
import de.westnordost.streetcomplete.quests.bus_stop_shelter.BusStopShelterAnswer.COVERED
import de.westnordost.streetcomplete.quests.bus_stop_shelter.BusStopShelterAnswer.NO_SHELTER
import de.westnordost.streetcomplete.quests.bus_stop_shelter.BusStopShelterAnswer.SHELTER

class AddBusStopShelter : OsmFilterQuestType<BusStopShelterAnswer>() {

    override val elementFilter = """
        nodes with
        (
          (public_transport = platform and ~bus|trolleybus|tram ~ yes)
          or
          (highway = bus_stop and public_transport != stop_position)
        )
        and physically_present != no and naptan:BusStopType != HAR
        and !covered
        and (!shelter or shelter older today -4 years)
    """
    /* Not asking again if it is covered because it means the stop itself is under a large
       building or roof building so this won't usually change */

    override val changesetComment = "Specify whether bus stops have shelters"
    override val wikiLink = "Key:shelter"
    override val icon = R.drawable.ic_quest_bus_stop_shelter
    override val achievements = listOf(PEDESTRIAN)

    override fun getTitle(tags: Map<String, String>) = R.string.quest_busStopShelter_title2

    override fun createForm() = AddBusStopShelterForm()

    override fun applyAnswerTo(answer: BusStopShelterAnswer, tags: Tags, timestampEdited: Long) {
        when (answer) {
            SHELTER -> tags.updateWithCheckDate("shelter", "yes")
            NO_SHELTER -> tags.updateWithCheckDate("shelter", "no")
            COVERED -> {
                tags.remove("shelter")
                tags["covered"] = "yes"
            }
        }
    }
}

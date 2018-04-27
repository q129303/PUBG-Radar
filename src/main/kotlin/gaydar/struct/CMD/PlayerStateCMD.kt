package gaydar.struct.CMD

import gaydar.GameListener
import gaydar.deserializer.channel.ActorChannel.Companion.attacks
import gaydar.deserializer.channel.ActorChannel.Companion.selfID
import gaydar.deserializer.channel.ActorChannel.Companion.selfStateID
import gaydar.deserializer.channel.ActorChannel.Companion.uniqueIds
import gaydar.register
import gaydar.struct.*
import gaydar.struct.Item.Companion.simplify
import gaydar.util.debugln
import gaydar.util.tuple2
import java.util.concurrent.ConcurrentHashMap

val playerNames = ConcurrentHashMap<NetworkGUID, String>()
val playerNumKills = ConcurrentHashMap<NetworkGUID, Long>()

object PlayerStateCMD : GameListener
{
  init
  {
    register(this)
  }

  override fun onGameOver()
  {
    uniqueIds.clear()
    attacks.clear()
    selfID = NetworkGUID(0)
    selfStateID = NetworkGUID(0)
    playerNumKills.clear()
  }

  fun process(actor : Actor, bunch : Bunch, repObj : NetGuidCacheObject?, waitingHandle : Int, data : HashMap<String, Any?>) : Boolean
  {
    //try
    //{
      actor as PlayerState
      with(bunch) {
        // println("WAITING HANDLE; $waitingHandle")
        when (waitingHandle)
        {
          //APlayerState
          16   -> propertyBool() //bFromPreviousLevel
          17   -> propertyBool() //bIsABot
          18   -> propertyBool() //bIsInactive
          19   -> propertyBool() //bIsSpectator
          20   -> propertyBool() //bOnlySpectator
          21   -> propertyByte() //Ping
          22   -> propertyInt() //PlayerId
          23   -> actor.name = propertyString()
          24   -> propertyFloat() //Score
          25   -> propertyInt() //StartTime
          26   -> uniqueIds[propertyNetId()] = actor.netGUID
          27   -> propertyString() //AccountId
          28   -> propertyBool() //bIsInAircraft
          29   -> propertyBool() //bIsZombie
          30   -> attacks.add(tuple2(uniqueIds[propertyString()]!!, actor.netGUID)) //CurrentAttackerPlayerNetId
          31   -> propertyFloat() //LastHitTime
          32   -> propertyInt() //MyGameScoreInTeam
          33   -> readInt(5) //ObserverAuthorityType
          34   -> propertyFloat() //struct FTslPlayerScores PlayerScores | ScoreByDamage
          35   -> propertyFloat() //ScoreByKill
          36   -> propertyFloat() //ScoreByRanking
          37   -> propertyFloat() //ScoreFactor (end struct)
          38   -> playerNumKills[actor.netGUID] = propertyUInt32() //struct FTslPlayerStatistics PlayerStatistics | NumKills (end struct)
          39   -> propertyUInt32() //struct FTslPlayerStatisticsForOwner PlayerStatisticsForOwner | HeadShots
          40   -> propertyFloat() //LongestDistanceKill
          41   -> propertyFloat() //TotalGivenDamages
          42   ->
          {
            propertyFloat() //TotalMovedDistanceMeter (end struct)
            selfStateID = actor.netGUID //only self will get this update
          }
          43   -> propertyInt() //Ranking
          44   ->
          {//TArray<struct FReplicatedCastableItem> ReplicatedCastableItems
            val arraySize = readUInt16()
            actor.castableItems.resize(arraySize)
            var index = readIntPacked()
            while (index != 0)
            {
              val idx = index - 1
              val arrayIdx = idx / 3
              val structIdx = idx % 3
              val element = actor.castableItems[arrayIdx] ?: tuple2("", 0)
              when (structIdx)
              {
                0 ->
                { //CastableItemClass
                  val (guid, castableItemClass) = readObject()
                  if (castableItemClass != null)
                    element._1 = simplify(castableItemClass.pathName)
                }
                1 ->
                { //ItemCount
                  val itemCount = readInt32()
                  element._2 = itemCount
                }
                2 ->
                { //ItemType
                  val ItemType = readInt(8)
                  val a = ItemType
                }
              }
              actor.castableItems[arrayIdx] = element
              index = readIntPacked()
            }
          }
          45   ->
          {//TArray<struct FReplicatedEquipableItem> ReplicatedEquipableItems
            val arraySize = readUInt16()
            actor.equipableItems.resize(arraySize)
            var index = readIntPacked()
            while (index != 0)
            {
              val idx = index - 1
              val arrayIdx = idx / 2
              val structIdx = idx % 2
              val element = actor.equipableItems[arrayIdx] ?: tuple2("", 0f)
              when (structIdx)
              {
                0 ->
                { //Durability
                  val durability = readFloat()
                  element._2 = durability
                  val a = durability
                }
                1 ->
                { //EquipableItemClass
                  val (guid, equipableItemClass) = readObject()
                  if (equipableItemClass != null)
                    element._1 = simplify(equipableItemClass.pathName)
                  val a = guid
                }
              }
              actor.equipableItems[arrayIdx] = element
              index = readIntPacked()
            }
          }
          46   -> propertyString() //ReportToken
          47   -> actor.teamNumber = readInt(100)
          else -> return ActorCMD.process(actor, bunch, repObj, waitingHandle, data)
        }
      }
      return true
    //}
    //catch (e : Exception)
    //{
    //  debugln { ("PlayerStateCMD is throwing somewhere: $e ${e.stackTrace} ${e.message} ${e.cause}") }
    //}
    //return false
  }
}
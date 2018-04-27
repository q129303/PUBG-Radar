package gaydar.struct.CMD

import gaydar.struct.Actor
import gaydar.struct.Bunch
import gaydar.struct.NetGuidCacheObject
import gaydar.struct.Team
import gaydar.util.debugln

object TeamCMD
{

  fun process(actor : Actor, bunch : Bunch, repObj : NetGuidCacheObject?, waitingHandle : Int, data : HashMap<String, Any?>) : Boolean
  {
    //try
    //{
      actor as Team
      with(bunch) {
        //      println("${actor.netGUID} $waitingHandle")
        when (waitingHandle)
        {
          16   -> readBit() //bIsDying
          17   -> readBit() //bIsGroggying
          18   -> readFloat() //BoostGauge
          19   -> readBit() //bQuitter
          20   -> actor.showMapMarker = readBit()
          21   -> readBit() //bUsingSquadInTeam
          22   -> readUInt8() //GroggyHealth
          23   -> readUInt8() //GroggyHealthMax
          24   -> readUInt8() //Health
          25   -> readUInt8() //HealthMax
          26   -> actor.mapMarkerPosition.set(readVector2D())
          27   -> actor.memberNumber = readUInt8()
          28   -> propertyVector100() //PlayerLocation
          29   -> propertyString() //PlayerName
          30   -> readRotationShort() //PlayerRotation
          31   -> readInt8() //SquadIndex
          32   -> readInt8() //SquadMemberIndex
          33   -> readInt(3) //TeamVehicleType
          34   -> readString() //UniqueId
          else -> return ActorCMD.process(actor, bunch, repObj, waitingHandle, data)
        }
        return true
      }
    //}
    //catch (e : Exception)
    //{
    //  debugln { ("TeamCMD is throwing somewhere: $e ${e.stackTrace} ${e.message} ${e.cause}") }
    //}
    //return false
  }
}
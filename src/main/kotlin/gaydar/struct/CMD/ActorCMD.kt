package gaydar.struct.CMD

import gaydar.GameListener
import gaydar.deserializer.ROLE_MAX
import gaydar.deserializer.channel.ActorChannel.Companion.actors
import gaydar.register
import gaydar.struct.Actor
import gaydar.struct.Bunch
import gaydar.struct.NetGuidCacheObject
import gaydar.struct.NetworkGUID
import gaydar.util.debugln
import java.util.concurrent.ConcurrentHashMap

object ActorCMD : GameListener
{
  init
  {
    register(this)
  }

  override fun onGameOver()
  {
    actorWithPlayerState.clear()
    playerStateToActor.clear()
  }

  val actorWithPlayerState = ConcurrentHashMap<NetworkGUID, NetworkGUID>()
  val playerStateToActor = ConcurrentHashMap<NetworkGUID, NetworkGUID>()

  fun process(actor : Actor, bunch : Bunch, repObj : NetGuidCacheObject?, waitingHandle : Int, data : HashMap<String, Any?>) : Boolean
  {
    //try
    //{
      with(bunch) {
        when (waitingHandle)
        {
          //AActor
          1    -> readObject() //struct FRepAttachment | AttachComponent
          2    ->
          {
            val (a, _) = readObject() //AttachParent
            val attachTo = if (a.isValid())
            {
              actors[a]?.attachChildren?.add(actor.netGUID)
              a
            }
            else null
            if (actor.attachParent != null)
              actors[actor.attachParent!!]?.attachChildren?.remove(actor.netGUID)
            actor.attachParent = attachTo
          }
          3    -> propertyName() //AttachSocket
          4    -> propertyVector100() //LocationOffset
          5    -> propertyVector100() //RelativeScale3D
          6    -> readRotationShort() //RotationOffset (end struct)
          7    -> readBit() //bCanBeDamaged
          8    -> readBit() //bHidden
          9    -> readBit() //bReplicateMovement
          10   -> readBit() //bTearOff
          11   -> propertyObject() //Instigator
          12   -> // Owner
          {
            val (netGUID, _) = readObject()
            actor.owner = if (netGUID.isValid()) netGUID else null
          }
          13   -> readInt(ROLE_MAX) //RemoteRole
          14   -> repMovement(actor) //struct FRepMovement
          15   -> readInt(ROLE_MAX) //Role
          else -> return false
        }
        return true
      }
    //}
    //catch (e : Exception)
    //{
    //  debugln { ("ActorState is throwing somewhere: $e ${e.stackTrace} ${e.message} ${e.cause}") }
    //}
    //return false
  }
}
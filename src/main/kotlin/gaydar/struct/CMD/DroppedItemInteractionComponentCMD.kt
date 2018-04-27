package gaydar.struct.CMD

import com.badlogic.gdx.math.Vector2
import gaydar.deserializer.channel.ActorChannel.Companion.droppedItemCompToItem
import gaydar.deserializer.channel.ActorChannel.Companion.droppedItemLocation
import gaydar.struct.Actor
import gaydar.struct.Bunch
import gaydar.struct.NetGuidCacheObject
import gaydar.util.debugln

object DroppedItemInteractionComponentCMD
{
  fun process(actor : Actor, bunch : Bunch, repObj : NetGuidCacheObject?, waitingHandle : Int, data : HashMap<String, Any?>) : Boolean
  {
    //try
    //{
      with(bunch) {
        when (waitingHandle)
        {
          //UActorComponent
          1    -> readBit() //bIsActive
          2    -> readBit() //bReplicates
          //USceneComponent
          3    ->
          {//TArray<class USceneComponent*> AttachChildren
            readUInt16()
            var index = readIntPacked()
            while (index != 0)
            {
              readObject()
              index = readIntPacked()
            }
          }
          4    -> readObject() //AttachParent
          5    -> readName() //AttachSocketName
          6    -> readBit() //bAbsoluteLocation
          7    -> readBit() //bAbsoluteRotation
          8    -> readBit() //bAbsoluteScale
          9    -> readBit() //bReplicatesAttachment
          10   -> readBit() //bReplicatesAttachmentReference
          11   -> readBit() //bVisible
          12   ->
          {
            val relativeLocation = propertyVector()
            data["relativeLocation"] = Vector2(relativeLocation.x, relativeLocation.y)
          }
          13   ->
          {
            val relativeRotation = readRotationShort()
            data["relativeRotation"] = relativeRotation.y
          }
          14   ->
          {
            val relativeScale3D = propertyVector()
            val a = relativeScale3D
          }
          //DroppedItemInteractionComponent
          15   ->
          {
            val (itemGUID, _) = readObject()
            val (loc, _) = droppedItemLocation[itemGUID] ?: return true
            droppedItemCompToItem[repObj!!.outerGUID] = itemGUID
            val relativeLocation = data["relativeLocation"] as Vector2
            val relativeRotation = data["relativeRotation"] as Float
            loc.add(relativeLocation.x, relativeLocation.y, relativeRotation)
          }
          else -> return false
        }
      }
      return true
    //}
    //catch (e : Exception)
    //{
    //  debugln { ("DroppedItemInteractionComp is throwing somewhere: $e ${e.stackTrace} ${e.message} ${e.cause}") }
    //}
    //return false
  }
}
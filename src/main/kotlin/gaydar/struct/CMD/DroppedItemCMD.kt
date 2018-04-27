package gaydar.struct.CMD

import gaydar.deserializer.channel.ActorChannel.Companion.droppedItemToItem
import gaydar.struct.Actor
import gaydar.struct.Bunch
import gaydar.struct.NetGuidCacheObject
import gaydar.util.debugln

object DroppedItemCMD
{

  fun process(actor : Actor, bunch : Bunch, repObj : NetGuidCacheObject?, waitingHandle : Int, data : HashMap<String, Any?>) : Boolean
  {
    //try
    //{
      with(bunch) {
        when (waitingHandle)
        {
          16   ->
          {
            val (itemguid, item) = readObject()
            droppedItemToItem[actor.netGUID] = itemguid
          }
          17   ->
          {//struct FSkinData SkinData | SkinTargetDatas TArray<struct FSkinTargetData> | struct FName TargetName, class USkinDataConfig* SkinDataConfig
            readUInt16() //arraySize
            var index = readIntPacked()
            while (index != 0)
            {
              when ((index - 1) % 2)
              {
                0 -> readObject() //SkinDataConfig
                1 -> readName() //TargetName
              }
              index = readIntPacked()
            }
          }
          else -> ActorCMD.process(actor, bunch, repObj, waitingHandle, data)
        }
        return true
      }
    //}
    //catch (e : Exception)
    //{
    //  debugln { ("DroppedItemCMD is throwing somewhere: $e ${e.stackTrace} ${e.message} ${e.cause}") }
    //}
    //return false
  }
}
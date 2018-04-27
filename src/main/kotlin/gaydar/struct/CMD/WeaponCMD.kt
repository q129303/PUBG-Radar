package gaydar.struct.CMD

import gaydar.deserializer.channel.ActorChannel.Companion.firing
import gaydar.struct.Actor
import gaydar.struct.Bunch
import gaydar.struct.NetGuidCacheObject
import gaydar.struct.Weapon
import gaydar.util.debugln
import gaydar.util.tuple2

object WeaponCMD
{
  fun process(actor : Actor, bunch : Bunch, repObj : NetGuidCacheObject?, waitingHandle : Int, data : HashMap<String, Any?>) : Boolean
  {
    //try
    //{
      actor as Weapon
      with(bunch) {
        when (waitingHandle)
        {
          //ATslWeapon
          16   ->
          {//TArray<class UClass*> AttachedItemClasses
            val arraySize = readUInt16()
            val equippedWeapons = IntArray(arraySize)
            var index = readIntPacked()
            while (index != 0)
            {
              val (netguid, item) = readObject()
              equippedWeapons[index - 1] = netguid.value
              //if (netguid.isValid())
              //  println("$actor has weapon  [$netguid](${weapons[netguid.value]?.Type})")
              index = readIntPacked()
            }
          }
          17   -> readObject() //MyPawn
          18   ->
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
          //ATslWeapon_Gun
          19   -> actor.ammoPerClip = propertyInt()
          20   -> actor.isHipped = readBit()
          21   ->
          {
            val CurrentAmmoInClip = propertyInt()
            if (CurrentAmmoInClip < actor.currentAmmoInClip) //Firing
              actor.owner?.apply {
                firing.add(tuple2(this, System.currentTimeMillis()))
              }
            actor.currentAmmoInClip = CurrentAmmoInClip
          }
          22   -> actor.currentZeroLevel = propertyInt()
          23   -> actor.firingModeIndex = propertyInt()
          //ATslWeapon_Trajectory
          else -> return ActorCMD.process(actor, bunch, repObj, waitingHandle, data)
        }
        return true
      }
    //}
    //catch (e : Exception)
    //{
    //  debugln { ("WeaponCMD is throwing somewhere: $e ${e.stackTrace} ${e.message} ${e.cause}") }
    //}
    //return false
  }
}
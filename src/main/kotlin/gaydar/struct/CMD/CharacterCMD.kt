package gaydar.struct.CMD

import gaydar.GameListener
import com.badlogic.gdx.math.Vector3
import gaydar.deserializer.byteRotationScale
import gaydar.deserializer.channel.ActorChannel.Companion.playerStateToActor
import gaydar.register
import gaydar.struct.*
import gaydar.struct.CMD.ActorCMD.actorWithPlayerState
import gaydar.util.debugln
import java.util.concurrent.ConcurrentHashMap

var selfDirection = 0f
val selfCoords = Vector3()

object CharacterCMD : GameListener
{
  init
  {
    register(this)
  }

  override fun onGameOver()
  {
    actorHealth.clear()
    spectatedCount.clear()
  }

  val actorHealth = ConcurrentHashMap<NetworkGUID, Float>()
  val spectatedCount = ConcurrentHashMap<NetworkGUID, Int>()

  fun process(actor : Actor, bunch : Bunch, repObj : NetGuidCacheObject?, waitingHandle : Int, data : HashMap<String, Any?>) : Boolean
  {
    //try
    //{
      actor as Character
      with(bunch) {
        when (waitingHandle)
        {
          17   ->
          {
            val (playerStateGUID, playerState) = propertyObject() //PlayerState
            if (playerStateGUID.isValid() && !actor.playerStateID.isValid())
            {
              actorWithPlayerState[actor.netGUID] = playerStateGUID
              playerStateToActor[playerStateGUID] = actor.netGUID
              actor.playerStateID = playerStateGUID
            }
          }
          //ACharacter
          19   -> propertyFloat() //AnimRootMotionTranslationScale
          20   -> propertyBool() //bIsCrouched
          21   -> propertyInt() //JumpMaxCount
          22   -> propertyFloat() //JumpMaxHoldTime
          23   -> propertyName() //struct FBasedMovementInfo ReplicatedBasedMovement | BoneName
          24   -> propertyBool() //bRelativeRotation
          25   -> propertyBool() //bServerHasBaseComponent
          26   -> propertyBool() //bServerHasVelocity
          27   -> propertyVector100() //Location
          28   -> propertyObject() //MovementBase
          29   -> readRotationShort() //Rotation | end struct
          30   -> propertyByte() //ReplicatedMovementMode
          31   -> propertyFloat() //ReplicatedServerLastTransformUpdateTimeStamp
          32   -> propertyVector10() //struct FRepRootMotionMontage RepRootMotion | Acceleration
          33   -> propertyObject() //AnimMontage
          34   ->
          { //struct FRootMotionSourceGroup AuthoritativeRootMotion
            val bHasAdditiveSources = readBit()
            val bHasOverrideSources = readBit()
            val lastPreAdditiveVelocity = propertyVector10()
            val bIsAdditiveVelocityApplied = readBit()
            val flags = readUInt8()
          }
          35   -> propertyBool() //bIsActive
          36   -> propertyBool() //bRelativePosition
          37   -> propertyBool() //bRelativeRotation
          38   -> propertyVector10() //LinearVelocity
          39   -> propertyVector100() //Location
          40   -> propertyObject() //MovementBase
          41   -> propertyName() //MovementBaseBoneName
          42   -> propertyFloat() //Position
          43   -> readRotationShort() //Rotation
          //AMutableCharacter
          44   ->
          {//InstanceDescriptor
            readUInt16()
            var index = readIntPacked()
            while (index != 0)
            {
              val value = readUInt8()
              index = readIntPacked()
            }
          }
          //ATslCharacter
          45   -> propertyVectorNormal() //AimOffsets
          46   -> propertyBool() //bAimStateActive
          47   -> propertyBool() //bIsActiveRagdollActive
          48   -> propertyBool() //bIsAimingRemote
          49   -> propertyBool() //bIsCoatEquipped
          50   -> propertyBool() //bIsDemoVaulting_CP
          51   -> propertyBool() //bIsFirstPersonRemote
          52   -> actor.isGroggying = propertyBool()
          53   -> propertyBool() //bIsHoldingBreath
          54   -> propertyBool() //bIsInVehicleRemote
          55   -> propertyBool() //bIsPeekLeft
          56   -> propertyBool() //bIsPeekRight
          57   -> actor.isReviving = propertyBool()
          58   -> propertyBool() //bIsScopingRemote
          59   -> propertyBool() //bIsThirdPerson
          60   -> propertyBool() //bIsThrowHigh
          61   -> propertyBool() //bIsWeaponObstructed
          62   -> propertyBool() //bIsZombie
          63   -> actor.boostGauge = propertyFloat()
          64   -> propertyFloat() //BoostGaugeMax
          65   -> propertyBool() //bServerFinishedVault
          66   -> propertyFloat() //BuffFinalSpreadFactor
          67   -> propertyBool() //bUseRightShoulderAiming
          68   -> propertyBool() //bWantsToCancelVault
          69   -> propertyBool() //bWantsToRollingLeft
          70   -> propertyBool() //bWantsToRollingRight
          71   -> propertyBool() //bWantsToRun
          72   -> propertyBool() //bWantsToSprint
          73   -> propertyBool() //bWantsToSprintingAuto
          74   -> propertyByte() //CharacterState
          75   -> propertyByte() //CurrentWeaponZoomLevel
          76   -> actor.groggyHealth = propertyFloat()
          77   -> propertyFloat() //GroggyHealthMax
          78   -> readRotationShort() //GunDirectionSway
          79   ->
          {
            val Health = propertyFloat()
            actor.health = Health
            actorHealth[actor.netGUID] = Health
          }
          80   -> propertyFloat() //HealthMax
          81   -> propertyBool() //IgnoreRotation
          82   -> propertyObject() //InventoryFacade
          83   -> propertyFloat() //struct FTakeHitInfo | ActualDamage
          84   -> propertyVector() //AttackerLocation
          85   -> propertyName() //AttackerWeaponName
          86   -> propertyBool() //bKilled
          87   -> propertyName() //BoneName
          88   -> propertyBool() //bPointDamage
          89   -> propertyBool() //bRadialDamage
          90   -> propertyFloat() //DamageMaxRadius
          91   -> propertyVectorQ() //DamageOrigin
          92   -> propertyObject() //DamageType
          93   -> propertyByte() //EnsureReplicationByte
          94   -> propertyObject() //PlayerInstigator
          95   -> propertyVectorQ() //RelHitLocation
          96   -> propertyByte() //ShotDirPitch
          97   -> propertyByte() //ShotDirYaw (end struct)
          98   -> readObject() //NetOwnerController
          99   -> readInt(4) //PreReplicatedStanceMode
          100  -> readInt(8) //Remote_CastAnim
          101  -> propertyFloat() //ReviveCastingTime
          102  -> readInt(8) //ShoesSoundType
          103  -> spectatedCount[actor.netGUID] = propertyInt() //SpectatedCount
          104  -> readInt(4) //TargetingType
          105  ->
		  {
		    val (teamsId) = propertyObject()
		    actor.teamID = teamsId
		  }
          106  -> readObject() //VehicleRiderComponent
          107  -> propertyObject() //WeaponProcessor
          else -> return APawnCMD.process(actor, bunch, repObj, waitingHandle, data)
        }
        return true
      }
    //}
    //catch (e : Exception)
    //{
    //  debugln { ("CharacterCMD is throwing somewhere: $e ${e.stackTrace} ${e.message} ${e.cause}") }
    //}
    //return false
  }
}
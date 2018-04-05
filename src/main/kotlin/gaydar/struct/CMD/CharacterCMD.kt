package gaydar.struct.CMD

import com.badlogic.gdx.math.Vector3
import gaydar.deserializer.byteRotationScale
import gaydar.deserializer.channel.ActorChannel.Companion.playerStateToActor
import gaydar.struct.*
import gaydar.struct.CMD.ActorCMD.actorWithPlayerState
import gaydar.util.debugln
import java.util.concurrent.ConcurrentHashMap

var selfDirection = 0f
val selfCoords = Vector3()

object CharacterCMD
{
  val actorHealth = ConcurrentHashMap<NetworkGUID, Float>()
  fun process(actor : Actor, bunch : Bunch, repObj : NetGuidCacheObject?, waitingHandle : Int, data : HashMap<String, Any?>) : Boolean
  {
    try
    {
      actor as Character
      with(bunch) {
        when (waitingHandle)
        {
          16   ->
          {
            val (playerStateGUID, playerState) = propertyObject()
            if (playerStateGUID.isValid() && !actor.playerStateID.isValid())
            {
              actorWithPlayerState[actor.netGUID] = playerStateGUID
              playerStateToActor[playerStateGUID] = actor.netGUID
              actor.playerStateID = playerStateGUID
            }
          }
        //ACharacter//struct FBasedMovementInfo
          19   ->
          {
            val MovementBase = propertyObject()
            val b = MovementBase
          }
          20   ->
          {
            val BoneName = propertyName()
            val b = BoneName
          }
          21   ->
          {
            val Location = propertyVector100()
            val b = Location
          }
          22   ->
          {
            val Rotation = readRotationShort()
            val b = Rotation
          }//propertyRotator()
          23   ->
          {
            val bServerHasBaseComponent = propertyBool()
            val b = bServerHasBaseComponent
          }
          24   ->
          {
            val bRelativeRotation = propertyBool()
            val b = bRelativeRotation
          }
          25   ->
          {
            val bServerHasVelocity = propertyBool()
            val b = bServerHasVelocity
          }//end struct FBasedMovementInfo
          26   ->
          {
            val AnimRootMotionTranslationScale = propertyFloat()
            val b = AnimRootMotionTranslationScale
          }
          27   ->
          {
            val ReplicatedServerLastTransformUpdateTimeStamp = propertyFloat()
            val b = ReplicatedServerLastTransformUpdateTimeStamp
          }
          28   ->
          {
            val ReplicatedMovementMode = propertyByte()
            val b = ReplicatedMovementMode
          }
          29   ->
          {
            val bIsCrouched = propertyBool()
            val b = bIsCrouched
          }
          30   ->
          {
            val JumpMaxHoldTime = propertyFloat()
            val b = JumpMaxHoldTime
          }
          31   ->
          {
            val JumpMaxCount = propertyInt()
            val b = JumpMaxCount
          }
        //struct FRepRootMotionMontage RepRootMotion;
          32   ->
          {
            val bIsActive = propertyBool()
            val b = bIsActive
          }
          33   ->
          {
            val AnimMontage = propertyObject()
            val b = AnimMontage
          }
          34   ->
          {
            val Position = propertyFloat()
            val b = Position
          }
          35   ->
          {
            val Location = propertyVector100()
            val b = Location
          }
          36   ->
          {
            val Rotation = readRotationShort()
            val b = Rotation
          }//propertyRotator()
          37   ->
          {
            val MovementBase = propertyObject()
            val b = MovementBase
          }
          38   ->
          {
            val MovementBaseBoneName = propertyName()
            val b = MovementBaseBoneName
          }
          39   ->
          {
            val bRelativePosition = propertyBool()
            val b = bRelativePosition
          }
          40   ->
          {
            val bRelativeRotation = propertyBool()
            val b = bRelativeRotation
          }
          41   ->
          {//player
            val bHasAdditiveSources = readBit()
            val bHasOverrideSources = readBit()
            val lastPreAdditiveVelocity = propertyVector10()
            val bIsAdditiveVelocityApplied = readBit()
            val flags = readUInt8()
          }
          42   ->
          {
            val Acceleration = propertyVector10()
            val b = Acceleration
          }
          43   ->
          {
            val LinearVelocity = propertyVector10()
            val b = LinearVelocity
          }
        //AMutableCharacter
          44   ->
          {//InstanceDescriptor
            val arrayNum = readUInt16()
            var index = readIntPacked()
            while (index != 0)
            {
              val value = readUInt8()
              index = readIntPacked()
            }
          }
        //ATslCharacter
          45   ->
          {
            val remote_CastAnim = readInt(8)
            val a = remote_CastAnim
          }
          46   ->
          {//new name in 3.7.27.18 CurrentWeaponZoomLevel, old name CurrentVariableZoomLevel
            val CurrentWeaponZoomLevel = propertyInt()
            val b = CurrentWeaponZoomLevel
          }
          47   ->
          {
            val BuffFinalSpreadFactor = propertyFloat()
            val b = BuffFinalSpreadFactor
          }
          48   ->
          {
            val InventoryFacade = propertyObject()
            val b = InventoryFacade
          }
          49   ->
          {
            val WeaponProcessor = propertyObject()
            val b = WeaponProcessor
          }
          50   ->
          {
            val CharacterState = propertyByte()
            val b = CharacterState
          }
          51   ->
          {
            val bIsScopingRemote = propertyBool()
            val b = bIsScopingRemote
          }
          52   ->
          {
            val bIsAimingRemote = propertyBool()
          }
          53   ->
          {
            val bIsFirstPersonRemote = propertyBool()
            val b = bIsFirstPersonRemote
          }
          54   ->
          {
            val bIsInVehicleRemote = propertyBool()
            val b = bIsInVehicleRemote
          }
          55   ->
          {//new for 3.7.27.18
            val SpectatedCount = propertyInt()
            val b = SpectatedCount
          }
          56   ->
          {
            val (teamsId) = propertyObject()
              actor.teamID = teamsId
          }
          57   ->
          {//struct FTakeHitInfo
            val ActualDamage = propertyFloat()
//          println("ActualDamage=$ActualDamage")
          }
          58   ->
          {
            val damageType = propertyObject()
//          println("damageType=$damageType")
          }
          59   ->
          {
            val PlayerInstigator = propertyObject()
//          if (PlayerInstigator.first in actors)
//            println("PlayerInstigator=${actors[PlayerInstigator.first]}")
          }
          60   ->
          {
            val DamageOrigin = propertyVectorQ()
//          println("DamageOrigin=$DamageOrigin")
          }
          61   ->
          {
            val RelHitLocation = propertyVectorQ()
//          println("RelHitLocation=$RelHitLocation")
          }
          62   ->
          {
            val BoneName = propertyName()
//          println("BoneName=$BoneName")
          }
          63   ->
          {
            val DamageMaxRadius = propertyFloat()
//          println("DamageMaxRadius=$DamageMaxRadius")
          }
          64   ->
          {
            var ShotDirPitch = propertyByte()
            val a = ShotDirPitch * byteRotationScale
//          println("ShotDirPitch=$a")
          }
          65   ->
          {
            val ShotDirYaw = propertyByte()
            val a = ShotDirYaw * byteRotationScale
//          println("ShotDirYaw=$a")
          }
          66   ->
          {
            val bPointDamage = propertyBool()
//          println("bPointDamage=$bPointDamage")
          }
          67   ->
          {
            val bRadialDamage = propertyBool()
//          println("bRadialDamage=$bRadialDamage")
          }
          68   ->
          {
            val bKilled = propertyBool()
//          println("bKilled=$bKilled")
          }
          69   ->
          {
            val EnsureReplicationByte = propertyByte()
            val b = EnsureReplicationByte
          }
          70   ->
          {
            val AttackerWeaponName = propertyName()
//          println("AttackerWeaponName=$AttackerWeaponName")
          }
          71   ->
          {
            val AttackerLocation = propertyVector()
          }
          72   ->
          {
            val TargetingType = readInt(4)
            val a = TargetingType
          }
          73   ->
          {
            val reviveCastingTime = propertyFloat()
            val a = reviveCastingTime
          }
          74   ->
          {
            val bWantsToRun = propertyBool()
            val b = bWantsToRun
          }
          75   ->
          {
            val bWantsToSprint = propertyBool()
            val b = bWantsToSprint
          }
          76   ->
          {
            val bWantsToSprintingAuto = propertyBool()
            val b = bWantsToSprintingAuto
          }
          77   ->
          {
            val bWantsToRollingLeft = propertyBool()
            val b = bWantsToRollingLeft
          }
          78   ->
          {
            val bWantsToRollingRight = propertyBool()
            val b = bWantsToRollingRight
          }
          79   ->
          {
            val bIsPeekLeft = propertyBool()
            val b = bIsPeekLeft
          }
          80   ->
          {
            val bIsPeekRight = propertyBool()
            val b = bIsPeekRight
          }
          81   ->
          {
            val IgnoreRotation = propertyBool()
            val b = IgnoreRotation
          }
          82   ->
          {
            val bIsGroggying = propertyBool()
            actor.isGroggying = bIsGroggying
          }
          83   ->
          {
            val bIsThirdPerson = propertyBool()
            val b = bIsThirdPerson
          }
          84   ->
          {
            val bIsReviving = propertyBool()
            actor.isReviving = bIsReviving
          }
          85   ->
          {
            val bIsWeaponObstructed = propertyBool()
            val b = bIsWeaponObstructed
          }
          86   ->
          {
            val bIsCoatEquipped = propertyBool()
            val b = bIsCoatEquipped
          }
          87   ->
          {
            val bIsZombie = propertyBool()
            val b = bIsZombie
          }
          88   ->
          {
            val bIsThrowHigh = propertyBool()
            val b = bIsThrowHigh
          }
          89   ->
          {
            val bUseRightShoulderAiming = propertyBool()
            val b = bUseRightShoulderAiming
          }
          90   ->
          {
            val GunDirectionSway = readRotationShort()//propertyRotator()
            val b = GunDirectionSway
          }
          91   ->
          {
            val AimOffsets = propertyVectorNormal()
            val b = AimOffsets
          }
          92   ->
          {
            val NetOwnerController = readObject()
            val b = NetOwnerController
          }
          93   ->
          {
            val bAimStateActive = propertyBool()
            val b = bAimStateActive
          }
          94   ->
          {
            val bIsHoldingBreath = propertyBool()
            val b = bIsHoldingBreath
          }
          95   ->
          {
            val health = propertyFloat()
            actor.health = health
            actorHealth[actor.netGUID] = health

//          println("health=$health")
          }
          96   ->
          {
            val healthMax = propertyFloat()
//          println("health max=$healthMax")
          }
          97   ->
          {
            val GroggyHealth = propertyFloat()
            actor.groggyHealth = GroggyHealth
          }
          98   ->
          {
            val GroggyHealthMax = propertyFloat()
//          println("GroggyHealthMax=$GroggyHealthMax")
          }
          99   ->
          {
            val BoostGauge = propertyFloat()
            actor.boostGauge = BoostGauge
          }
          100   ->
          {
            val BoostGaugeMax = propertyFloat()
//          println("BoostGaugeMax=$BoostGaugeMax")
          }
          101  ->
          {
            val ShoesSoundType = readInt(8)
            val b = ShoesSoundType
          }
          102  ->
          {
            val VehicleRiderComponent = readObject()
            val b = VehicleRiderComponent
          }
          103  ->
          {
            val bIsActiveRagdollActive = propertyBool()
            val b = bIsActiveRagdollActive
          }
          104  ->
          {
            val PreReplicatedStanceMode = readInt(4)
            val b = PreReplicatedStanceMode
          }
          105  ->
          {
            val bServerFinishedVault = propertyBool()
            val b = bServerFinishedVault
          }
          106  ->
          {
            val bWantsToCancelVault = propertyBool()
            val b = bWantsToCancelVault
          }
          107  ->
          {
            val bIsDemoVaulting_CP = propertyBool()
            val b = bIsDemoVaulting_CP
          }
          else -> return APawnCMD.process(actor, bunch, repObj, waitingHandle, data)
        }
        return true
      }
    }
    catch (e : Exception)
    {
      debugln { ("CharacterCMD is throwing somewhere: $e ${e.stackTrace} ${e.message} ${e.cause}") }
    }
    return false
  }
}
package gaydar.struct.CMD

import com.badlogic.gdx.math.Vector2
import gaydar.GameListener
import gaydar.register
import gaydar.struct.Actor
import gaydar.struct.Bunch
import gaydar.struct.NetGuidCacheObject
import gaydar.util.debugln

object GameStateCMD : GameListener
{
  init
  {
    register(this)
  }

  override fun onGameOver()
  {
    SafetyZonePosition.setZero()
    SafetyZoneRadius = 0f
    SafetyZoneBeginPosition.setZero()
    SafetyZoneBeginRadius = 0f
    PoisonGasWarningPosition.setZero()
    PoisonGasWarningRadius = 0f
    RedZonePosition.setZero()
    RedZoneRadius = 0f
    TotalWarningDuration = 0f
    ElapsedWarningDuration = 0f
    TotalReleaseDuration = 0f
    ElapsedReleaseDuration = 0f
    NumJoinPlayers = 0
    NumAlivePlayers = 0
    NumAliveTeams = 0
    RemainingTime = 0
    MatchElapsedMinutes = 0
    isTeamMatch = false
  }

  var isTeamMatch = false
  var TotalWarningDuration = 0f
  var ElapsedWarningDuration = 0f
  var RemainingTime = 0
  var MatchElapsedMinutes = 0
  val SafetyZonePosition = Vector2()
  var SafetyZoneRadius = 0f
  val SafetyZoneBeginPosition = Vector2()
  var SafetyZoneBeginRadius = 0f
  val PoisonGasWarningPosition = Vector2()
  var PoisonGasWarningRadius = 0f
  val RedZonePosition = Vector2()
  var RedZoneRadius = 0f
  var TotalReleaseDuration = 0f
  var ElapsedReleaseDuration = 0f
  var NumJoinPlayers = 0
  var NumAlivePlayers = 0
  var NumAliveTeams = 0

  fun process(actor : Actor, bunch : Bunch, repObj : NetGuidCacheObject?, waitingHandle : Int, data : HashMap<String, Any?>) : Boolean
  {
    //try
    //{
      with(bunch) {
        when (waitingHandle)
        {
          16   -> propertyBool() //bReplicatedHasBegunPlay
          17   -> propertyObject() //GameModeClass
          18   -> propertyFloat() //ReplicatedWorldTimeSeconds
          19   -> propertyObject() //SpectatorClass
          20   -> propertyInt() //ElapsedTime
          21   -> propertyName() //MatchState
          22   -> propertyBool() //bCanKillerSpectate
          23   -> propertyBool() //bCanShowLastCircleMark
          24   -> propertyBool() //bIsCustomGame
          25   -> propertyBool() //bIsGasRelease
          26   -> propertyBool() //bIsTeamElimination
          27   -> isTeamMatch = propertyBool()
          28   -> propertyBool() //bIsWarMode
          29   -> propertyBool() //bIsWinnerZombieTeam
          30   -> propertyBool() //bIsWorkingBlueZone
          31   -> propertyBool() //bIsZombieMode
          32   -> propertyBool() //bShowAircraftRoute
          33   -> propertyBool() //bShowLastCircleMark
          34   -> propertyBool() //bTimerPaused
          35   -> propertyBool() //bUseWarRoyaleBluezone
          36   -> propertyBool() //bUseXboxUnauthorizedDevice
          37   -> propertyBool() //bUsingSquadInTeam
          38   -> ElapsedReleaseDuration = propertyFloat()
          39   -> ElapsedWarningDuration = propertyFloat()
          40   -> propertyInt() //GoalScore
          41   -> readVector2D() //LastCirclePosition
          42   -> MatchElapsedMinutes = propertyInt() //MatchElapsedMinutes
          43   -> propertyFloat() //MatchElapsedTimeSec
          44   -> propertyString() //MatchId
          45   -> propertyString() //MatchShortGuid
          46   -> readInt(3) //MatchStartType
          47   -> propertyFloat() //NextRespawnTimeSeconds 
          48   -> NumAlivePlayers = propertyInt()
          49   -> NumAliveTeams = propertyInt()
          50   -> propertyInt() //NumAliveZombiePlayers
          51   -> NumJoinPlayers = propertyInt()
          52   -> propertyInt() //NumStartPlayers
          53   -> propertyInt() //NumStartTeams
          54   -> propertyInt() //NumTeams
          55   ->
          {
            val pos = propertyVector()
            PoisonGasWarningPosition.set(pos.x, pos.y)
          }
          56   -> PoisonGasWarningRadius = propertyFloat()
          57   ->
          {
            val pos = propertyVector()
            RedZonePosition.set(pos.x, pos.y)
          }
          58   -> RedZoneRadius = propertyFloat()
          59   -> RemainingTime = propertyInt()
          60   ->
          {
            val pos = propertyVector()
            SafetyZoneBeginPosition.set(pos.x, pos.y)
          }
          61   -> SafetyZoneBeginRadius = propertyFloat()
          62   ->
          {
            val pos = propertyVector()
            SafetyZonePosition.set(pos.x, pos.y)
          }
          63   -> SafetyZoneRadius = propertyFloat()
          64   ->
          {//TArray<int> TeamIds
            readUInt16()
            var index = readIntPacked()
            while (index != 0)
            {
              readInt()
              index = readIntPacked()
            }
          }
          65   ->
          {//TArray<int> TeamIndices
            readUInt16()
            var index = readIntPacked()
            while (index != 0)
            {
              readInt()
              index = readIntPacked()
            }
          }
          66   ->
          {//TArray<struct FString> TeamLeaderNames
            readUInt16()
            var index = readIntPacked()
            while (index != 0)
            {
              propertyString()
              index = readIntPacked()
            }
          }
          67   ->
          {//TArray<int> TeamScores
            readUInt16()
            var index = readIntPacked()
            while (index != 0)
            {
              readInt()
              index = readIntPacked()
            }
          }
          68   -> propertyFloat() //TimeLimitSeconds
          69   -> TotalReleaseDuration = propertyFloat()
          70   -> TotalWarningDuration = propertyFloat()
          else -> return ActorCMD.process(actor, bunch, repObj, waitingHandle, data)
        }
        return true
      }
    //}
    //catch (e : Exception)
    //{
    //  debugln { ("GameStateCMD is throwing somewhere: $e ${e.stackTrace} ${e.message} ${e.cause}") }
    //}
    //return false
  }
}
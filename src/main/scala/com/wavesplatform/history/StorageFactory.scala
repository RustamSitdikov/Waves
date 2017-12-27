package com.wavesplatform.history

import java.util.concurrent.locks.{ReentrantReadWriteLock => RWL}
import javax.sql.DataSource

import com.wavesplatform.database.SQLiteWriter
import com.wavesplatform.features.FeatureProvider
import com.wavesplatform.settings.WavesSettings
import com.wavesplatform.state2.reader.SnapshotStateReader
import com.wavesplatform.state2.{BlockchainUpdaterImpl, StateWriter}
import scorex.transaction._
import scorex.utils.Time

import scala.util.Try

object StorageFactory {

  def apply(settings: WavesSettings, ds: DataSource, time: Time): (NgHistory with DebugNgHistory, StateWriter with SnapshotStateReader, BlockchainUpdater, BlockchainDebugInfo) = {
    val stateWriter = new SQLiteWriter(ds)
    val bcu = new BlockchainUpdaterImpl(stateWriter, settings, time, ???)
    val history: NgHistory with DebugNgHistory with FeatureProvider = bcu.historyReader
    (history, history, stateWriter, bcu.bestLiquidState, bcu, bcu)
  }
}

package cz.utb.fai.soundboard.services

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer


object ExoPlayerObj {
    private var player: ExoPlayer? = null

    var lastPlayedUri: Uri? = null

    var isNowPlaying: Boolean = false

    fun play(context: Context, uri: Uri) {
        stop()

        lastPlayedUri = uri

        player = ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            play()

            isNowPlaying = true

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        onPlaybackFinished()
                    }
                }
            })
        }
    }

    fun stop() {
        player?.run {
            stop()
            release()
        }
        player = null

        isNowPlaying = false
    }

    private fun onPlaybackFinished() {
        isNowPlaying = false
    }

}
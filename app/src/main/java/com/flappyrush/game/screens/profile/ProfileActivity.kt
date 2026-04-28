package com.flappyrush.game.screens.profile

import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.flappyrush.auth.AuthManager
import com.flappyrush.data.models.Player
import com.flappyrush.data.repository.PlayerRepository
import com.flappyrush.game.competitive.Leaderboard

class ProfileActivity : AppCompatActivity() {

    private val auth = AuthManager()
    private val playerRepo = PlayerRepository()
    private val leaderboard = Leaderboard(playerRepo)

    private lateinit var usernameText: TextView
    private lateinit var rankText: TextView
    private lateinit var bestScoreText: TextView
    private lateinit var totalGamesText: TextView
    private lateinit var globalRankText: TextView
    private lateinit var leaderboardList: LinearLayout

    private var currentTab = Leaderboard.Tab.GLOBAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUI()
        loadData()
    }

    private fun buildUI() {
        val scroll = ScrollView(this)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 80, 40, 40)
            setBackgroundColor(android.graphics.Color.parseColor("#1A1A2E"))
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 16, 0, 0) }

        fun label(text: String, size: Float = 16f, color: String = "#AAAAAA") = TextView(this).apply {
            this.text = text; textSize = size
            setTextColor(android.graphics.Color.parseColor(color))
            gravity = Gravity.CENTER
        }

        usernameText   = label("...", 32f, "#FFFFFF")
        rankText       = label("...", 20f, "#FFD700")
        bestScoreText  = label("Best: ...", 18f, "#FFFFFF")
        totalGamesText = label("Games: ...", 18f, "#AAAAAA")
        globalRankText = label("Global rank: ...", 16f, "#FF6B35")

        // Tab buttons
        val tabRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val globalTab = Button(this).apply {
            text = "Global"; setOnClickListener { switchTab(Leaderboard.Tab.GLOBAL) }
            setBackgroundColor(android.graphics.Color.parseColor("#FF6B35"))
        }
        val friendsTab = Button(this).apply {
            text = "Friends"; setOnClickListener { switchTab(Leaderboard.Tab.FRIENDS) }
            setBackgroundColor(android.graphics.Color.parseColor("#455A64"))
        }
        tabRow.addView(globalTab, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
        tabRow.addView(friendsTab, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))

        leaderboardList = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }

        val backBtn = Button(this).apply {
            text = "← BACK"
            setBackgroundColor(android.graphics.Color.parseColor("#37474F"))
            setTextColor(android.graphics.Color.WHITE)
            setOnClickListener { finish() }
        }

        root.addView(usernameText, params)
        root.addView(rankText, params)
        root.addView(bestScoreText, params)
        root.addView(totalGamesText, params)
        root.addView(globalRankText, params)
        root.addView(label("— LEADERBOARD —", 14f, "#555555"), params)
        root.addView(tabRow, params)
        root.addView(leaderboardList, params)
        root.addView(backBtn, params)

        scroll.addView(root)
        setContentView(scroll)
    }

    private fun loadData() {
        val uid = auth.uid ?: return
        playerRepo.listenToPlayer(uid) { player ->
            player ?: return@listenToPlayer
            runOnUiThread { bindProfile(player) }
        }
        leaderboard.getPlayerRank(uid) { rank ->
            runOnUiThread { globalRankText.text = if (rank > 0) "Global rank: #$rank" else "Unranked" }
        }
        loadLeaderboard()
    }

    private fun bindProfile(player: Player) {
        usernameText.text = player.username.ifEmpty { "Guest" }
        rankText.text = "⭐ ${player.getRankFromScore()}"
        bestScoreText.text = "Best score: ${player.bestScore}"
        totalGamesText.text = "Games played: ${player.totalGames}"
    }

    private fun switchTab(tab: Leaderboard.Tab) {
        currentTab = tab
        loadLeaderboard()
    }

    private fun loadLeaderboard() {
        val uid = auth.uid ?: return
        leaderboardList.removeAllViews()

        val onEntries: (List<Leaderboard.LeaderboardEntry>) -> Unit = { entries ->
            runOnUiThread {
                leaderboardList.removeAllViews()
                entries.take(50).forEach { entry ->
                    val row = buildLeaderboardRow(entry)
                    leaderboardList.addView(row)
                }
            }
        }

        if (currentTab == Leaderboard.Tab.GLOBAL) {
            leaderboard.getGlobal(onResult = onEntries)
        } else {
            playerRepo.listenToPlayer(uid) { player ->
                val friends = player?.friendUids?.keys?.toList() ?: emptyList()
                leaderboard.getFriends(uid, friends, onResult = onEntries)
            }
        }
    }

    private fun buildLeaderboardRow(entry: Leaderboard.LeaderboardEntry): LinearLayout {
        val bgColor = when {
            entry.isCurrentUser -> "#1E3A2F"
            entry.rank == 1     -> "#2E2A00"
            else                -> "#16213E"
        }
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(android.graphics.Color.parseColor(bgColor))
            setPadding(24, 20, 24, 20)

            val rankView = TextView(this@ProfileActivity).apply {
                text = "#${entry.rank}"
                textSize = 16f
                setTextColor(android.graphics.Color.parseColor(
                    if (entry.rank == 1) "#FFD700" else "#AAAAAA"
                ))
                width = 100
            }
            val nameView = TextView(this@ProfileActivity).apply {
                text = entry.player.username.ifEmpty { "Guest" }
                textSize = 16f
                setTextColor(android.graphics.Color.WHITE)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }
            val scoreView = TextView(this@ProfileActivity).apply {
                text = entry.player.bestScore.toString()
                textSize = 16f
                setTextColor(android.graphics.Color.parseColor("#FF6B35"))
                gravity = Gravity.END
            }

            addView(rankView)
            addView(nameView)
            addView(scoreView)
        }
    }
}


package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

class GameFragment : Fragment() {
    private lateinit var binding: GameFragmentBinding
    //gameViewModel variable init
    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_fragment,
            container,
            false
        )
        //Log.i("GameFragment", "Called ViewModelProvider !!")
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.gameViewModel=gameViewModel
        binding.lifecycleOwner=this

        //Live data update the UI

        gameViewModel.eventGameFinish.observe(this, Observer {hasFinished->
            if(hasFinished){
                gameFinished()
                gameViewModel.onGameFinishComplete()
            }

        })

        return binding.root

    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(gameViewModel.score.value?:0)
        findNavController(this).navigate(action)
    }


}

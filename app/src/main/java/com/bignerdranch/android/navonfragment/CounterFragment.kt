package com.bignerdranch.android.navonfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bignerdranch.android.navonfragment.databinding.FragmentCounterBinding

class CounterFragment : Fragment() {

    private lateinit var binding:FragmentCounterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //для инициализации интерфейса используется метод nCreateView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCounterBinding.inflate(inflater,container,false)

        binding.counterTextView.text = getString(R.string.screen_label,getCounterValue())
        binding.quoteTextView.text = getQuote()

        binding.launchNextButton.setOnClickListener {
            launchNext()
        }
        binding.goBackButton.setOnClickListener {
            goBack()
        }
        //метода setContentView() во фрагментах нет,поэтому нужно просто вернуть то что наинициализировали
        return binding.root
    }


    private fun launchNext() {
        //через requireActivity() получаем текущую активити к которой присоединён текущий фрагмент
        //узнаём номер активти и увеличиваем на 1,там же запрашиваем цитату
        val fragment = CounterFragment.newInstance(
            counterValue = (requireActivity() as MainActivity).getScreensCount() + 1,
            quote = (requireActivity() as MainActivity).createQuote()
        )
        //внутри фрагмента вызывает не support а parentFragmentManager
        // .addToBackStack(null) нужен для того что бы фрагменты формировали стэк
        // это необходимо для навигации вперёд и назад(иначе кнопка назад не будет работать)
        // replace будет вместо add,потому что мы заменяем экраны а не наслаиваем один на другой(иначе они будут накладываться друг на друга(поверх))
        parentFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer,fragment)
            .commit()
    }

    private fun goBack() {
        requireActivity().onBackPressed()
    }

    private fun getCounterValue():Int = requireArguments().getInt(ARG_COUNTER_VALUE)

    private fun getQuote():String = requireArguments().getString(ARG_QUOTE)!!

    //что бы передать во фрагмент данные,нужно использовать Bundle а не Intent
    //Принято делать метод newInstance
    companion object{
        private const val ARG_COUNTER_VALUE = "ARG_COUNTER_VALUE"

        private const val ARG_QUOTE = "ARG_QUOTE"

        fun newInstance(counterValue:Int,quote:String):CounterFragment{
            val args = Bundle().apply {
                putInt(ARG_COUNTER_VALUE,counterValue)
                putString(ARG_QUOTE,quote)
            }
            val fragment = CounterFragment()
            //создали бандл,в который положили данные по ключам
            //экземпляр фрагмента и кладём аргументы в .arguments
            fragment.arguments = args
            return fragment
        }
    }
    //Так нужно делать потому что у всех фрагментов должен быть только пустой конструктор,это нужно системе
    //аргументы сохранятся даже после поворота экрана и тд
}
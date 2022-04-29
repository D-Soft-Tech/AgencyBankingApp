package com.keystone.agencybankingapp.ui.di.fundWallet

import androidx.fragment.app.Fragment
import com.keystone.agencybankingapp.ui.view.main.fundwalletflow.AddCard
import com.keystone.agencybankingapp.ui.view.main.fundwalletflow.FundWalletFirstPage
import com.keystone.agencybankingapp.ui.view.main.fundwalletflow.OtpPage
import javax.inject.Singleton

@Singleton
class FundWalletScreensImplementationClass : FundWalletScreens {
    override fun fundWallets(): ArrayList<Fragment> {
        return arrayListOf(
            FundWalletFirstPage(),
            AddCard(),
            OtpPage()
        )
    }
}
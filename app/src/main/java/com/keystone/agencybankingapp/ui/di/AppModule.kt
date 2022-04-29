package com.keystone.agencybankingapp.ui.di

import com.keystone.agencybankingapp.ui.di.fundWallet.FundWalletScreensImplementationClass
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesScreensToFundWalletViewPager(): FundWalletScreensImplementationClass {
       return FundWalletScreensImplementationClass()
    }
}
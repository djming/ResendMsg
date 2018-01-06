package com.levi.resendmsg.main

import com.levi.resendmsg.IBasePresenter
import com.levi.resendmsg.IBaseView

/**
 * Created by levi on 2018/1/6.
 * Copyright © 2017 levi.
 * All rights reserved.
 * Contact:dengjinming9668@gmail.com
 */
 
class MainContractor {
    interface IMainPresenter : IBasePresenter {

    }

    interface IMainView : IBaseView<IMainPresenter> {

    }
}
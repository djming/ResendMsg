package com.levi.resendmsg

/**
 * Created by levi on 2018/1/6.
 * Copyright © 2017 levi.
 * All rights reserved.
 * Contact:dengjinming9668@gmail.com
 */


interface IBaseView<T : IBasePresenter> {
    var presenter : T
}

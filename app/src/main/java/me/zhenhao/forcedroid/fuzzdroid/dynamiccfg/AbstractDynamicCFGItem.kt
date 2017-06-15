package me.zhenhao.forcedroid.fuzzdroid.dynamiccfg

import me.zhenhao.forcedroid.fuzzdroid.tracing.TraceItem



/**
 * Created by tom on 6/9/17.
 */
abstract class AbstractDynamicCFGItem : TraceItem {

    constructor() : super()

    constructor(lastExecutedStatement: Int) : super(lastExecutedStatement)

    companion object {
        private val serialVersionUID = -5500762826791899632L
    }

}
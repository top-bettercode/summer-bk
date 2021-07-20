package top.bettercode.lang.keyword

import java.util.*

/**
 * 每个节点的值隐含在父节点children Map的key上，根节点为一个无值空节点
 *
 * @author Peter Wu
 */
data class CharNode(
    /**
         * 父节点
         */
        var parent: top.bettercode.lang.keyword.CharNode? = null,
    /**
         * 字符所在层级，即匹配的字符串的长度;
         */
        var length: Int = 0) {

    /**
     * 子节点
     */
    private var children: MutableMap<Char, top.bettercode.lang.keyword.CharNode> = HashMap(0)

    /**
     * 匹配失败时，指向较短的匹配，如：‘我是谁’，匹配失败时，指向，‘我是’节点
     */
    var failNode: top.bettercode.lang.keyword.CharNode? = null


    val isEnd: Boolean
        get() {
            return children.isEmpty()
        }


    // function
    fun addChild(character: Char): top.bettercode.lang.keyword.CharNode {
        var charNode: top.bettercode.lang.keyword.CharNode? = children[character]
        if (charNode == null) {
            val length = this.length + 1
            charNode = top.bettercode.lang.keyword.CharNode(this, length)
            children[character] = charNode
        }

        return charNode
    }

    fun childNodes(): Collection<top.bettercode.lang.keyword.CharNode> {
        return children.values
    }

    operator fun get(c: Char): top.bettercode.lang.keyword.CharNode? {
        return children[c]
    }
}

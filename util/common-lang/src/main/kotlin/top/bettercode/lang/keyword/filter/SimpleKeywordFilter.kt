package top.bettercode.lang.keyword.filter

import top.bettercode.lang.keyword.CharNode
import top.bettercode.lang.keyword.MatchType
import top.bettercode.lang.keyword.replace.DefaultReplaceStrategy
import top.bettercode.lang.keyword.replace.ReplaceStrategy

/**
 * 简单实现
 *
 * @author Peter Wu
 */
open class SimpleKeywordFilter(
    val root: top.bettercode.lang.keyword.CharNode = top.bettercode.lang.keyword.CharNode(),
    /**
         * 设置匹配模式
         */
        var matchType: top.bettercode.lang.keyword.MatchType = top.bettercode.lang.keyword.MatchType.LONG,
    /**
         * 设置替换策略
         */
        var strategy: ReplaceStrategy = DefaultReplaceStrategy()) :
    top.bettercode.lang.keyword.filter.KeywordFilter {


    override fun replace(text: String): String {
        var last = root
        val result = StringBuilder()
        val words = text.toCharArray()
        val matchShort = matchType == top.bettercode.lang.keyword.MatchType.SHORT
        var i = 0
        while (i < words.size) {
            val word = words[i]

            var length = last.length
            val lastIndex = i - length
            val end = i == words.size - 1
            var containLast = false
            val charNode = last[word]
            if (charNode != null) {
                last = charNode
                length++
                containLast = true
            }
            val lastEnd = last.isEnd
            if (last === root) {
                result.append(word)
            } else if (containLast && matchShort && lastEnd) {
                result
                        .append(strategy.replaceWith(words.copyOfRange(lastIndex, lastIndex + length)))
                last = root
            } else if (!containLast || end) {
                if (lastEnd) {
                    result.append(strategy
                            .replaceWith(words.copyOfRange(lastIndex, lastIndex + length)))
                    if (!containLast) {
                        i--
                    }
                } else {
                    // 未结束，找短匹配
                    if (matchShort) {
                        i = lastIndex
                        result.append(words[i])
                    } else {
                        val failNode = last.failNode
                        if (failNode === root) {
                            i = lastIndex
                            result.append(words[i])
                        } else {
                            val failLength = failNode!!.length
                            i = lastIndex + failLength - 1
                            result.append(strategy.replaceWith(words.copyOfRange(lastIndex, lastIndex + failLength)))
                        }
                    }
                }
                last = root
            }
            i++
        }
        return result.toString()
    }

    override fun compile(keywords: Collection<String>) {
        addKeywords(keywords)
        // 构建失败节点
        buildFailNode(root)
    }

    /**
     * 构建char树，作为搜索的数据结构。
     *
     * @param keywords 关键字
     */
    open fun addKeywords(keywords: Collection<String>) {
        // 加入关键字字符串
        for (keyword in keywords) {
            if (keyword.isBlank()) {
                throw IllegalArgumentException("过滤关键词不能为空！")
            }
            val charArray = keyword.toCharArray()
            var node = root
            for (aCharArray in charArray) {
                node = node.addChild(aCharArray)
            }
        }
    }

    /**
     * 构建失败节点
     *
     * @param node 节点
     */
    open fun buildFailNode(node: top.bettercode.lang.keyword.CharNode) {
        doFailNode(node)
        val childNodes = node.childNodes()
        for (childNode in childNodes) {
            buildFailNode(childNode)
        }
    }

    private fun doFailNode(node: top.bettercode.lang.keyword.CharNode) {
        if (node === root) {
            return
        }
        var parent = node.parent

        while (!parent!!.isEnd && parent !== root) {
            parent = parent.parent
        }
        node.failNode = parent
    }

}

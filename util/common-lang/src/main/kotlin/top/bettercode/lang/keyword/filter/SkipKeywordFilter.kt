package top.bettercode.lang.keyword.filter

import top.bettercode.lang.keyword.CharNode
import top.bettercode.lang.keyword.MatchType
import top.bettercode.lang.keyword.replace.DefaultReplaceStrategy
import top.bettercode.lang.keyword.replace.ReplaceStrategy
import java.util.*

/**
 * 可忽略中间的特殊字符,比如：过*滤，中的*
 *
 * @author Peter Wu
 */
class SkipKeywordFilter(
    root: top.bettercode.lang.keyword.CharNode = top.bettercode.lang.keyword.CharNode(),
    /**
         * 设置匹配模式
         */
        matchType: top.bettercode.lang.keyword.MatchType = top.bettercode.lang.keyword.MatchType.LONG,
    /**
         * 设置替换策略
         */
        strategy: ReplaceStrategy = DefaultReplaceStrategy()) : top.bettercode.lang.keyword.filter.SimpleKeywordFilter(root, matchType, strategy) {

    private val skipChars = HashSet<Char>(0)
    private var skip = false

    override fun replace(text: String): String {
        if (skip) {
            var last = root
            val result = StringBuilder()
            val words = text.toCharArray()
            val matchShort = matchType == top.bettercode.lang.keyword.MatchType.SHORT
            val ignoredWords = ArrayList<Int>()
            var i = 0
            while (i < words.size) {
                val word = words[i]

                var length = last.length
                length += ignoredWords.size
                val lastIndex = i - length
                val skipChar = skipChars.contains(word)
                val end = i == words.size - 1
                var containLast = false
                if (!skipChar) {
                    val charNode = last[word]
                    if (charNode != null) {
                        last = charNode
                        length++
                        containLast = true
                    }
                } else if (!end) {
                    ignoredWords.add(i)
                    i++
                    continue
                }
                val lastEnd = last.isEnd
                if (last === root) {
                    for (integer in ignoredWords) {
                        result.append(words[integer])
                    }
                    result.append(word)
                    ignoredWords.clear()
                } else if (containLast && matchShort && lastEnd) {
                    result.append(strategy
                            .replaceWith(words.copyOfRange(lastIndex, lastIndex + length)))
                    ignoredWords.clear()
                    last = root
                } else if (!containLast || end) {
                    if (lastEnd) {
                        result.append(strategy.replaceWith(words.copyOfRange(lastIndex, lastIndex + length)))
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
                                var failLength = failNode!!.length
                                i = lastIndex + failLength - 1

                                var count = 0
                                for (integer in ignoredWords) {
                                    if (integer > i) {
                                        break
                                    }
                                    count++
                                }
                                failLength += count
                                result.append(strategy.replaceWith(words.copyOfRange(lastIndex, lastIndex + failLength)))
                            }
                        }
                    }
                    last = root
                    ignoredWords.clear()
                }
                i++
            }
            return result.toString()
        } else {
            return super.replace(text)
        }
    }

    /**
     * 增加过滤字符
     *
     * @param chars 过滤的字符
     */
    fun addSkipChar(chars: Collection<Char>?) {
        if (null != chars && !chars.isEmpty()) {
            this.skipChars.addAll(chars)
            this.skip = true
        }
    }

}
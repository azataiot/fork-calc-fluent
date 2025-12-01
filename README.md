# CalcFluent – 让 BigDecimal 真正支持「多步表达式」的金融计算器

**简体中文 | [English](#english-version)**

[![Maven Central](https://img.shields.io/maven-central/v/io.calcfluent/calc-fluent?label=Maven%20Central)](https://search.maven.org/artifact/io.calcfluent/calc-fluent)
[![Java 17+](https://img.shields.io/badge/Java-17%2B-blue)](https://www.oracle.com/java/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**核心价值一句话：**  
让 Java 的 `BigDecimal` 像写 Excel 公式一样自然地表达「多步复合运算」，而不再是只能一句一句单向操作。

## 痛点 → 解法

| 你现在的写 BigDecimal 时的真实痛苦                               | CalcFluent 怎么解决（一行代码搞定）                                                                                           |
|---------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| 公式稍微复杂一点就要写 5~10 行临时变量                          | 链式 + 完整括号 → 一条语句写完整个业务公式                                                                                 |
| 括号优先级全靠手动拆成多行，改一次公式要改好几处               | 支持任意层级嵌套括号，改公式只改这一行                                                                                     |
| 改错一个括号或优先级，整个金额就错了，调试到吐血               | 全局调试开关 `enableCalcDebug()` → 自动打印完整公式，错在哪里一目了然                                                      |
| 想复用中间结果只能再写一遍同样的链式代码                      | 公式写一次，后面随便拿 `result()` 复用                                                                                     |
| 业务方看不懂你写的十行 `BigDecimal` 运算                      | 公式和 Excel、需求文档长得一模一样，业务、测试、审核都能看懂                                                               |

## 5 秒上手（完整示例在 `io.calcfluent.demo.Demo`）

## 为什么这才是金融场景的「正确打开方式」

- 公式和业务文档、Excel、需求描述完全一致 → 可审计、可对账、可复现
- 无括号时强制从左到右计算 → 杜绝「乘除优先于加减」带来的隐形 bug
- 禁止 `double`/`float` 输入 → 从编译期就阻止精度丢失
- 支持任意深度括号嵌套 → 再复杂的分润、阶梯计费、组合优惠都不怕

## 运行要求

- Java 17 及以上
- 零强制依赖（SLF4J 为 optional）

## 许可证

MIT – 随便商用、闭源、修改，只需保留版权声明即可。

---

## English Version

# CalcFluent – Make BigDecimal Truly Support Multi-step Expressions

**English | [简体中文](#calcfluent--让-bigdecimal-真正支持多步表达式的金融计算器)**

The real pain point of `BigDecimal` has never been “losing precision”,  
but **you can only do one operation at a time**.

CalcFluent solves exactly this:  
Let you write complex multi-step financial formulas in **one single readable expression**, just like Excel.

### From This (painful)

```java
BigDecimal t1 = orderAmount.subtract(fullReduceThreshold.multiply(fullReduceRate));
BigDecimal t2 = t1.multiply(vipDiscount);
BigDecimal t3 = t2.add(inviteBase.multiply(inviteRate));
BigDecimal result = t3.multiply(taxRate).setScale(2, HALF_UP);
```

### To This (happy)

```java
BigDecimal result = Calculator.startWith(orderAmount)
    .subtractParenthesisThen(fullReduceThreshold).multiply(fullReduceRate).rightParenthesis()
    .multiply(vipDiscount)
    .addParenthesisThen(inviteBase).multiply(inviteRate).rightParenthesis()
    .multiply(taxRate)
    .round(2)
    .result();
```

One line of debug prints the full formula automatically — no more manual logging hell.

### Key Benefits

- Formula looks exactly like business spec / Excel / requirement doc → auditable & reviewable
- No parentheses → strict left-to-right evaluation → eliminates hidden precedence bugs forever
- `double`/`float` forbidden at compile time → zero chance of precision loss
- Unlimited parentheses nesting → handles any complex tiered pricing, revenue sharing, combined promotions

### Quick Start

Full example: see `io.calcfluent.demo.Demo`

Requires Java 17+

MIT License

---
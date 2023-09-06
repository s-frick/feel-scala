/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.feel.impl.builtin

import org.camunda.feel.impl.{EvaluationResultMatchers, FeelEngineTest}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
  * @author Philipp
  */
class BuiltinFunctionsTest
    extends AnyFlatSpec
    with Matchers
    with FeelEngineTest
    with EvaluationResultMatchers {

  "A built-in function" should "return null if arguments doesn't match" in {

    evaluateExpression("date(true)") should returnNull()
    evaluateExpression("number(false)") should returnNull()
  }

  "A not() function" should "negate Boolean" in {

    evaluateExpression(" not(true) ") should returnResult(false)
    evaluateExpression(" not(false) ") should returnResult(true)
  }

  "A is defined() function" should "return true if the value is present" in {

    evaluateExpression("is defined(null)") should returnResult(true)

    evaluateExpression("is defined(1)") should returnResult(true)
    evaluateExpression("is defined(true)") should returnResult(true)
    evaluateExpression("is defined([])") should returnResult(true)
    evaluateExpression("is defined({})") should returnResult(true)
    evaluateExpression(""" is defined( {"a":1}.a ) """) should returnResult(true)
  }

  // see: https://github.com/camunda/feel-scala/issues/695
  ignore should "return false if a variable doesn't exist" in {
    evaluateExpression("is defined(a)") should returnResult(false)
    evaluateExpression("is defined(a.b)") should returnResult(false)
  }

  // see: https://github.com/camunda/feel-scala/issues/695
  ignore should "return false if a context entry doesn't exist" in {
    evaluateExpression("is defined({}.a)") should returnResult(false)
    evaluateExpression("is defined({}.a.b)") should returnResult(false)
  }

  "A get or else(value: Any, default: Any) function" should "return the value if not null" in {

    evaluateExpression("get or else(3, 1)") should returnResult(3)
    evaluateExpression("""get or else("value", "default")""") should returnResult("value")
    evaluateExpression("get or else(value:3, default:1)") should returnResult(3)
  }

  it should "return the default param if value is null" in {

    evaluateExpression("get or else(null, 1)") should returnResult(1)
    evaluateExpression("""get or else(null, "default")""") should returnResult("default")
    evaluateExpression("get or else(value:null, default:1)") should returnResult(1)
  }

  it should "return null if both value and default params are null" in {

    evaluateExpression("get or else(null, null)") should returnNull()
    evaluateExpression("get or else(value:null, default:null)") should returnNull()
  }
}

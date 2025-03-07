/* Copyright 2019 EPFL, Lausanne
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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

package calculator

import org.scalatest._
import flatspec._

class Tests extends AnyFlatSpec with Inside {

  "Parser" should "be LL(1)" in {
    assert(CalcParser.expr.isLL1)
  }

  it should "be able to parse some strings" in {
    val result = CalcParser(CalcLexer("1 + 3 * (5! / 7) + 42"))

    assert(result.nonEmpty)

    val parsed = result.get

    inside(parsed) {
      case BinaryExpr('+', BinaryExpr('+', one, mult), fortytwo) => {
        assert(one == LitExpr(1))
        assert(fortytwo == LitExpr(42))
        inside(mult) {
          case BinaryExpr('*', three, BinaryExpr('/', UnaryExpr('!', five), seven)) => {
            assert(three == LitExpr(3))
            assert(five == LitExpr(5))
            assert(seven == LitExpr(7))
          }
        }
      }
    }
  }
}
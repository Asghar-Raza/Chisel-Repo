package narrow
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

class NarrowingOperationsRVVTest extends AnyFreeSpec with ChiselScalatestTester {
  "NarrowingOperationsRVV" in {
    test(new NarrowingOperationsRVV(4)) { c =>
      // Test inputs
      val testInputsX = Seq(4.U, 8.U, 16.U, 32.U)
      val testInputY = 2.U

      // Expected outputs for each operation
      val expectedOutputsAdd = Seq(6.U, 10.U, 18.U, 34.U)
      val expectedOutputsSub = Seq(2.U, 6.U, 14.U, 30.U)
      val expectedOutputsAnd = Seq(0.U, 0.U, 0.U, 0.U)
      val expectedOutputsOr = Seq(6.U, 10.U, 18.U, 34.U)
      val expectedOutputsRsub = Seq(4.U.asUInt, 2.U.asUInt, 10.U.asUInt, 26.U.asUInt)
      val expectedOutputsXor = Seq(8.U, 12.U, 20.U, 36.U)

      // Poke inputs
      c.io.vx.zip(testInputsX).foreach { case (input, value) => input.poke(value) }
      c.io.vy.poke(testInputY)

      // Test each operation
      for ((operation, expectedOutputs) <- Seq(
        (0.U, expectedOutputsAdd),
        (1.U, expectedOutputsSub),
        (2.U, expectedOutputsAnd),
        (3.U, expectedOutputsOr),
        (4.U, expectedOutputsRsub),
        (5.U, expectedOutputsXor)
      )) {
        c.io.operation.poke(operation)
        c.clock.step()
        c.io.result.zip(expectedOutputs).foreach { case (output, expected) => output.expect(expected) }
      }
    }
  }
}

// class NarrowingOperationsRVVMainTest extends AnyFreeSpec with ChiselScalatestTester {
//   "NarrowingOperationsRVVMain" in {
//     test(new NarrowingOperationsRVVMain(16, 128)) { c =>
//       // Define test inputs that fit within 128-bit range
//       val testInputs = Seq(
//         BigInt("12345678901234567890123456789012", 16),  // A 128-bit input value
//         BigInt("98765432109876543210987654321098", 16)   // Another 128-bit input value
//       )

//       // Expected 128-bit results after manual narrowing
//       val expectedResults = Seq(
//         BigInt("220582416", 16),  // Expected 128-bit result for the first input
//         BigInt("83247094", 16)    // Expected 128-bit result for the second input
//       )

//       // Initialize main module's output
//       c.io.outputVector.foreach(_.poke(0.U))

//       // Set the operation to multiplication (0)
//       c.mainModule.module.io.operation.poke(0.U)

//       // Poke the test inputs into the main module
//       c.mainModule.module.io.vr.zip(testInputs).foreach { case (input, value) =>
//         input.poke(value).U
//       }

//       // Ensure that the main module computes the expected results
//       c.clock.step()  // You may need to advance the clock depending on your design

//       // Check the results after the clock step
//       c.mainModule.module.io.outputVector.zip(expectedResults).foreach { case (output, expected) =>
//         output.expect(expected).U
//       }
//     }
//   }
// }
   
  

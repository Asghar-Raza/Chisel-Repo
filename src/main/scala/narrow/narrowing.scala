package narrow
import chisel3._
import chisel3.util._


class NarrowingOperationsRVV(vlen: Int) extends Module {
  val io = IO(new Bundle {
    val vx = Input(Vec(vlen, UInt(128.W)))
    val vy = Input(UInt(128.W))
    val operation = Input(UInt(3.W))
    val result = Output(Vec(vlen, UInt(128.W)))
  })

  // Perform operations based on the operation input
  when(io.operation === 0.U) {
    // vaddvx
    io.result := io.vx.map(_ + io.vy)
  } .elsewhen(io.operation === 1.U) {
    // vsubvx
    io.result := io.vx.map(_ - io.vy)
  } .elsewhen(io.operation === 2.U) {
    // vandvx
    io.result := io.vx.map(_ & io.vy)
  } .elsewhen(io.operation === 3.U) {
    // vorvx
    io.result := io.vx.map(_ | io.vy)
  } .elsewhen(io.operation === 4.U) {
    // vrsubvx
    io.result := io.vx.map(io.vy - _)
  } .elsewhen(io.operation === 5.U) {
    // vxorvx
    io.result := io.vx.map(_ ^ io.vy)
  } .otherwise {
    // Default case
    io.result := io.vx
  }
}

// class NarrowingOperationsRVVMain(vlen: Int, vectorWidth: Int) extends Module {
//   val io = IO(new Bundle {
//     val outputVector = Output(Vec(vlen, UInt(vectorWidth.W)))
//   })

//   // Create vector registers with initial values (128-bit UInt)
// val vr = Seq.fill(vlen)(BigInt("12345678901234567890123456789012", 16).U(128.W))

//   // Create an instance of the NarrowingOperationsRVV module
//   val mainModule = Module(new NarrowingOperationsRVV(vlen))

//   // Connect the vector registers to the module's input
//   mainModule.io.vx := VecInit(vr)

//   // Set the operation to multiplication (0)
//   mainModule.io.operation := 0.U

//   // Connect the module's output to the main output
//   io.outputVector := mainModule.io.vy
// }

// class NarrowingOperationsRVV(vlen: Int) extends Module {
//   // Define the number of vector elements (vlen) and the vector width (e.g., 128 bits)
//   val io = IO(new Bundle {
//     val vx = Input(Vec(vlen, UInt(128.W)))
//     val operation = Input(UInt(2.W))
//     val vy = Output(Vec(vlen, UInt(128.W)))
//   })
//   when(io.operation === 0.U) {
//     // When operation is 0, perform addition
//     io.vy := io.vx.map(_ + 1.U)
//   } .elsewhen(io.operation === 1.U) {
//     // When operation is 1, perform multiplication
//     io.vy := io.vx.map(_ * 2.U)
//   } .otherwise {
//     // Default case
//     io.vy := io.vx
//   }
//   // val vlen = 16
//   val vectorWidth = 128

//   // Create an instance of the main module
//   val module = Module(new NarrowingOperationsRVVMain(vlen, vectorWidth))

//   // Print the input and output for demonstration
//   println(s"Input Vector: ${module.vr.mkString(", ")}")
//   println(s"Operation: Multiplication (0)")
//   println(s"Output Vector: ${module.io.outputVector.mkString(", ")}")

// }


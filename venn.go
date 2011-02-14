/*
    Venn diagram

    Produce the union, intersection or difference of sets of lines contained within two files
    given as arguments to program.
*/

package main

import "fmt"
import . "strings"
import "os"
//import "io"
import "bufio"
//import "time"
//import . "strconv"
import "flag"
import . "sort"

/*
    Reads all lines in file and return as array
*/
func readLines(file_name string) StringArray {
	file, err := os.Open(file_name, os.O_RDONLY, 0666)
	defer file.Close()
	if file == nil {
        fmt.Println(err.String())
        os.Exit(1)
  }

  lines := make(StringArray, 0, 256)

  in := bufio.NewReader(file)
  s, err := in.ReadString('\n')
  for ; err != os.EOF; s, err = in.ReadString('\n') {
    s = TrimRight(s, "\n")
    lines = append(lines, s)
	}
  return lines
}

var Usage = func() {
  fmt.Fprintf(os.Stderr, "Usage of %s:\n", os.Args[0])
  fmt.Fprintf(os.Stderr, "%s COMMAND file1.txt file2.txt\n", os.Args[0])
  fmt.Fprintf(os.Stderr, "COMMANDS\n\tor\n\t\tcombine lines from both files\n\tand\n\t\tonly lines present in both files\n\txor\n\t\tonly lines not shared among files\n")
}

/*
 *  Mutable
 */
func unique(xs StringArray) StringArray {
  j := 1
  for i := 1; i < len(xs); i++ {
    if xs[i - 1] != xs[i] {
      xs[j] = xs[i]
      j++
    }
  }

  return xs[0:j]
}

/*
 * lines not shared
 */
func difference(as, bs StringArray) {
  as = unique(as)
  bs = unique(bs)
  asize := len(as)
  bsize := len(bs)

  i, j := 0, 0
  for i < asize && j < bsize {
    a := as[i]
    b := bs[j]
    if a > b {
      j++
      fmt.Println(b)
    } else if b > a {
      i++
      fmt.Println(a)
    } else {
      i++
      j++
    }
  }

  for ;i < asize; i++ {
    fmt.Println(as[i])
  }

  for ;j < bsize; j++ {
    fmt.Println(bs[j])
  }

}

/*
 * lines shared
 */
func intersect(alines, blines StringArray) {

  asize := len(alines)
  bsize := len(blines)

  prev := ""
  for i, j := 0, 0; i < asize && j < bsize; {
    a := alines[i]
    b := blines[j]
    if a > b {
      j++
    } else if b > a {
      i++
    } else {
      j++
      i++
      if a != prev {
        fmt.Println(a)
      }
      prev = a
    }
  }
}

/*
 * All lines in a and b combined. No duplicates
 */
func union(alines, blines StringArray) {

  asize := len(alines)
  bsize := len(blines)

  line := ""
  prev := ""
  for i, j := 0, 0; i < asize || j < bsize; {
    if i >= asize {
      line = blines[j]
      j++
    } else if j >= bsize {
      line = alines[i]
      i++
    } else if alines[i] <= blines[j] {
      line = alines[i]
      i++
    } else {
      line = blines[j]
      j++
    }
    if prev != line {
      fmt.Println(line)
    }
    prev = line
  }

}

func main() {
	flag.Parse()   // Scans the arg list and sets up flags
	if flag.NArg() != 3 {
    fmt.Fprintf(os.Stderr, "Expected 3 argument got %d\n", flag.NArg())
		Usage()
		os.Exit(0)
	}

	afiles  := flag.Arg(1)
  bfiles := flag.Arg(2)

  alines := readLines(afiles)
  blines := readLines(bfiles)

  alines.Sort()
  blines.Sort()

  switch cmd := flag.Arg(0); {
  case cmd == "xor":  
    difference(alines, blines)
  case cmd == "and":
    intersect(alines, blines)
  case cmd == "or":
    union(alines, blines)
  }
}


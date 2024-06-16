import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextGraphTest {
	// 创建一个TextGraph对象
	 private final TextGraph textGraph = new TextGraph();

	@BeforeEach
	public void setUp() {
		textGraph.inputGraph("src/main/java/input.txt");
		textGraph.showDirectedGraph(null);
	}

	@Test
	void testCalcShortestPath1() {
		// 创建一个ByteArrayOutputStream对象来捕获输出
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		// 调用calcShortestPath方法
		textGraph.calcShortestPath("this", "test");

		// 预期的输出
		String expected = "The shortest path from \"this\" to \"test\" is: this->is->a->test, with a distance of 6.\r\n";

		// 使用assertEquals验证结果是否与预期的输出匹配
		assertEquals(expected, outContent.toString());
	}

	@Test
	void testCalcShortestPath2() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		textGraph.calcShortestPath("the", "special");

		String expected = "The shortest path from \"the\" to \"special\" is: the->program->including->empty->lines->special, with a distance of 5.\r\n";

		assertEquals(expected, outContent.toString());
	}

	@Test
	void testCalcShortestPath3() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		textGraph.calcShortestPath("with", "and");

		String expected = "The shortest path from \"with\" to \"and\" is: with->commas->and, with a distance of 2.\r\n";

		assertEquals(expected, outContent.toString());
	}

	@Test
	void testCalcShortestPath4() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		textGraph.calcShortestPath("this", "@");

		String expected = "==================================================================================\n" +
						"The shortest path from \"this\" to other words are:\n" +
						"\r\n" +
						"The shortest path from \"this\" to \"uppercase\" is: this->is->a->test->uppercase, with a distance of 7.\r\n" +
						"The shortest path from \"this\" to \"commas\" is: this->is->a->test->file->with->commas, with a distance of 10.\r\n" +
						"The shortest path from \"this\" to \"for\" is: this->is->a->test->file->for, with a distance of 9.\r\n" +
						"The shortest path from \"this\" to \"numbers\" is: this->is->a->test->file->with->commas->and->numbers, with a distance of 12.\r\n" +
						"The shortest path from \"this\" to \"scenarios\" is: this->is->a->test->file->for->the->program->including->empty->lines->special->cases->to->cover->different->scenarios, with a distance of 20.\r\n" +
						"The shortest path from \"this\" to \"program\" is: this->is->a->test->file->for->the->program, with a distance of 11.\r\n" +
						"The shortest path from \"this\" to \"empty\" is: this->is->a->test->file->for->the->program->including->empty, with a distance of 13.\r\n" +
						"The shortest path from \"this\" to \"cover\" is: this->is->a->test->file->for->the->program->including->empty->lines->special->cases->to->cover, with a distance of 18.\r\n" +
						"The shortest path from \"this\" to \"onlyoneword\" is: this->is->a->test->file->with->commas->and->numbers->onlyoneword, with a distance of 13.\r\n" +
						"The shortest path from \"this\" to \"characters\" is: this->is->a->test->file->for->the->program->including->empty->lines->special->characters, with a distance of 16.\r\n" +
						"The shortest path from \"this\" to \"file\" is: this->is->a->test->file, with a distance of 8.\r\n" +
						"The shortest path from \"this\" to \"and\" is: this->is->a->test->file->with->commas->and, with a distance of 11.\r\n" +
						"The shortest path from \"this\" to \"mixedcase\" is: this->is->a->test->file->with->commas->and->numbers->onlyoneword->mixedcase, with a distance of 14.\r\n" +
						"The shortest path from \"this\" to \"text\" is: this->is->a->test->file->for->the->input->text, with a distance of 12.\r\n" +
						"The shortest path from \"this\" to \"semi\" is: this->is->a->test->file->with->commas->and->semi, with a distance of 12.\r\n" +
						"The shortest path from \"this\" to \"different\" is: this->is->a->test->file->for->the->program->including->empty->lines->special->cases->to->cover->different, with a distance of 19.\r\n" +
						"The shortest path from \"this\" to \"lines\" is: this->is->a->test->file->for->the->program->including->empty->lines, with a distance of 14.\r\n" +
						"The shortest path from \"this\" to \"a\" is: this->is->a, with a distance of 4.\r\n" +
						"The shortest path from \"this\" to \"cases\" is: this->is->a->test->file->for->the->program->including->empty->lines->special->cases, with a distance of 16.\r\n" +
						"The shortest path from \"this\" to \"including\" is: this->is->a->test->file->for->the->program->including, with a distance of 12.\r\n" +
						"The shortest path from \"this\" to \"test\" is: this->is->a->test, with a distance of 6.\r\n" +
						"The shortest path from \"this\" to \"lowercase\" is: this->is->a->test->uppercase->lowercase, with a distance of 8.\r\n" +
						"The shortest path from \"this\" to \"in\" is: this->is->a->test->file->for->the->program->including->empty->lines->special->cases->to->cover->different->scenarios->in, with a distance of 21.\r\n" +
						"The shortest path from \"this\" to \"dots\" is: this->is->a->test->file->with->dots, with a distance of 10.\r\n" +
						"The shortest path from \"this\" to \"is\" is: this->is, with a distance of 2.\r\n" +
						"The shortest path from \"this\" to \"repeatedword\" is: this->is->a->test->uppercase->lowercase->repeatedword, with a distance of 9.\r\n" +
						"The shortest path from \"this\" to \"it\" is: this->is->a->test->file->for->the->input->text->it, with a distance of 13.\r\n" +
						"The shortest path from \"this\" to \"the\" is: this->is->a->test->file->for->the, with a distance of 10.\r\n" +
						"The shortest path from \"this\" to \"special\" is: this->is->a->test->file->for->the->program->including->empty->lines->special, with a distance of 15.\r\n" +
						"The shortest path from \"this\" to \"input\" is: this->is->a->test->file->for->the->input, with a distance of 11.\r\n" +
						"The shortest path from \"this\" to \"with\" is: this->is->a->test->file->with, with a distance of 9.\r\n" +
						"The shortest path from \"this\" to \"contains\" is: this->is->a->test->file->for->the->input->text->it->contains, with a distance of 14.\r\n" +
						"The shortest path from \"this\" to \"various\" is: this->is->a->test->file->for->the->input->text->it->contains->various, with a distance of 15.\r\n" +
						"The shortest path from \"this\" to \"to\" is: this->is->a->test->file->for->the->program->including->empty->lines->special->cases->to, with a distance of 17.\r\n" +
						"==================================================================================\r\n";

		assertEquals(expected, outContent.toString());
	}

	@Test
	void testCalcShortestPath5() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		textGraph.calcShortestPath("dog", "test");

		String expected = "No \"dog\" in the graph!\r\n";

		assertEquals(expected, outContent.toString());
	}

	@Test
	void testCalcShortestPath6() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		textGraph.calcShortestPath("@", "test");

		String expected = "Invalid word1!\r\n";

		assertEquals(expected, outContent.toString());
	}

	@Test
	void testCalcShortestPath7() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		textGraph.calcShortestPath("this", "cat");

		String expected = "No \"cat\" in the graph!\r\n";

		assertEquals(expected, outContent.toString());
	}

	@Test
	void testCalcShortestPath8() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		textGraph.calcShortestPath("semi", "and");

		String expected = "No path from \"semi\" to \"and\"!\r\n";

		assertEquals(expected, outContent.toString());
	}
}
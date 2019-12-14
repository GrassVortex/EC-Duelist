package ECDuelist.Cards;

import ECDuelist.Settings.CardLibrary;
import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.generic.*;
import com.sun.org.apache.bcel.internal.util.JavaWrapper;

import java.util.HashMap;

public class CardFactory {

	private static HashMap<String, CardSettings> storedSettings;
	private static Generator generator;

	public static Card createCard(CardLibrary library, String cardId) {

		CardSettings settings = new CardSettings(library, cardId);
		settings.print();

		if (storedSettings == null) {
			storedSettings = new HashMap<String, CardSettings>();
			generator = new Generator();
		}
		storedSettings.put(cardId, settings);

		return generateCardClass(cardId);
	}

	private static Card generateCardClass(String cardId) {
		return generator.generateClass(cardId);
	}

	public static CardSettings loadSettings(String cardId) {
		return storedSettings.get(cardId);
	}


	public static class BaseTestClass {

	}

	private static class Generator
			  extends ClassLoader {

		public Generator() {
			super(ClassLoader.getSystemClassLoader());


		}

		public Card generateClass(String cardId) {

			//JavaWrapper jw = new JavaWrapper();

			String className = CardFactory.class.getPackage().getName() + ".Generated." + cardId;
			String superClass = Card.class.getName();
			//superClass = BaseTestClass.class.getName();
			//superClass = "java.lang.Object";

			System.out.println("superClass " + superClass);
			try {
				loadClass(superClass);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			ClassGen classGenerator =
					  new ClassGen(className,
								 superClass,
								 "",
								 Constants.ACC_PUBLIC | Constants.ACC_SUPER,
								 null);
			ConstantPoolGen cp = classGenerator.getConstantPool();

			classGenerator.addEmptyConstructor(Constants.ACC_PUBLIC);

//			InstructionList il = new InstructionList();
//			il.append(InstructionConstants.THIS); // Push `this'
//			il.append(new PUSH(cp, cardId));
//			il.append(new INVOKESPECIAL(cp.addMethodref(superClass,
//					  "<init>", "()V")));
//			il.append(InstructionConstants.RETURN);
//
//			MethodGen mg = new MethodGen(Constants.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, null,
//					  "<init>", className, il, cp);
//			mg.setMaxStack(1);
//			classGenerator.addMethod(mg.getMethod());

			final String cardNameField = "cardName";


			//FieldGen cardNameFieldGenerator = new FieldGen(Constants.ACC_PRIVATE | Constants.ACC_FINAL | Constants.ACC_STATIC, Type.STRING, cardNameField, constantPool);
			//classGenerator.addField(cardNameFieldGenerator.getField());

			byte[] classData = classGenerator.getJavaClass().getBytes();
			Class cardClass = defineClass(className, classData, 0, classData.length);
			resolveClass(cardClass);

			try {
				return (Card) cardClass.newInstance();
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			}
		}
	}
}

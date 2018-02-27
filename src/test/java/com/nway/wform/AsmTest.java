package com.nway.wform;

import java.io.IOException;

import org.junit.Test;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Opcodes;

import com.nway.platform.wform.component.impl.TextComponent;

public class AsmTest {

	@Test
	public void readClassSignature() {

		try {
			
			ClassReader cr = new ClassReader(TextComponent.class.getName());
			
			final String[] classTypeParameter = new String[1];
			
			cr.accept(new ClassVisitor(Opcodes.ASM5) {
				
				@Override
				public void visit(int version, int access, String name, String signature, String superName,
						String[] interfaces) {
					
					super.visit(version, access, name, signature, superName, interfaces);

					classTypeParameter[0] = signature
							.substring("Ljava/lang/Object;Lcom/nway/platform/wform/component/BaseComponent<L".length(),
									signature.length() - 3).replace('/', '.');
				}
			}, 2);
			
			System.out.println(classTypeParameter[0]);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}

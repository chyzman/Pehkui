package virtuoel.pehkui.server.command.arguments;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.util.CommandUtils;

public class ScaleModifierArgumentType implements ArgumentType<ScaleModifier>
{
	private static final Collection<String> EXAMPLES = ScaleRegistries.SCALE_MODIFIERS.keySet().stream().map(Identifier::toString).collect(Collectors.toList());
	public static final DynamicCommandExceptionType INVALID_ENTRY_EXCEPTION = new DynamicCommandExceptionType(arg -> new LiteralText("Unknown scale modifier '" + arg + "'"));
	
	@Override
	public ScaleModifier parse(StringReader stringReader) throws CommandSyntaxException
	{
		final Identifier identifier = Identifier.fromCommandInput(stringReader);
		return Optional.ofNullable(ScaleRegistries.getEntry(ScaleRegistries.SCALE_MODIFIERS, identifier)).orElseThrow(() -> INVALID_ENTRY_EXCEPTION.create(identifier));
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		return CommandUtils.suggestIdentifiersIgnoringNamespace(Pehkui.MOD_ID, ScaleRegistries.SCALE_MODIFIERS.keySet(), builder);
	}
	
	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
	
	public static ScaleModifierArgumentType scaleModifier()
	{
		return new ScaleModifierArgumentType();
	}
	
	public static ScaleModifier getScaleModifierArgument(CommandContext<ServerCommandSource> context, String name)
	{
		return context.getArgument(name, ScaleModifier.class);
	}
}
